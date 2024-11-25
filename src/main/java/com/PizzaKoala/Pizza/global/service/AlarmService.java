package com.PizzaKoala.Pizza.global.service;

import com.PizzaKoala.Pizza.global.repository.AlarmRepository;
import com.PizzaKoala.Pizza.global.repository.EmitterRepository;
import com.PizzaKoala.Pizza.member.repository.MemberRepository;
import com.PizzaKoala.Pizza.global.controller.request.AlarmRequest;
import com.PizzaKoala.Pizza.global.controller.response.Response;
import com.PizzaKoala.Pizza.global.entity.Alarm;
import com.PizzaKoala.Pizza.global.dto.AlarmArgs;
import com.PizzaKoala.Pizza.member.entity.Member;
import com.PizzaKoala.Pizza.global.exception.ErrorCode;
import com.PizzaKoala.Pizza.global.exception.PizzaAppException;
import com.PizzaKoala.Pizza.global.dto.AlarmDTO;
import com.PizzaKoala.Pizza.global.entity.enums.AlarmType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class AlarmService {
    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;
    private final AlarmRepository alarmRepository;
    private final static Long DEFAULT_TIMEOUT=60L * 1000 *60; //
    private final static String ALARM_NAME = "alarm";

    //send a notification to a specific member
    public void send(AlarmType alarmType,Long alarmId,Long memberId,AlarmArgs args) {
        emitterRepository.get(memberId).ifPresentOrElse(sseEmitter -> {
            try {
                // Construct the notification payload
                Map<String, Object> notification = new HashMap<>();
                notification.put("alarmId", alarmId);
                notification.put("alarmType", alarmType.name());

                notification.put("args", args);
                notification.put("createdDate", LocalDateTime.now().toString());

                // Send the event with the constructed data
                sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(ALARM_NAME).data(notification));
            } catch (IOException e) {
                emitterRepository.delete(memberId);
                throw new PizzaAppException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("No emitter found for memberId: " + memberId));
    }


    //connect the member to receive notification
    public SseEmitter connectAlarm(String email) {
        Member member=memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded ",email)));

        SseEmitter sseEmitter= new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(member.getId(), sseEmitter);

        sseEmitter.onCompletion(()->emitterRepository.delete(member.getId()));
        sseEmitter.onTimeout(()->emitterRepository.delete(member.getId()));

        try {
            sseEmitter.send(SseEmitter.event().id("id").name(ALARM_NAME).data("connect established"));
        } catch (IOException exception) {
            throw new PizzaAppException(ErrorCode.ALARM_CONNECT_ERROR);
        }
        return sseEmitter;
    }
    //save the alarm to the db and then send it.
    public void saveAndSend(AlarmRequest request) {
        //알림 데이터를 alarmArgs에 담아서 저장
        AlarmArgs args;
        if (request.getAlarmType() == AlarmType.NEW_FOLLOWER) {

            args = new AlarmArgs(request.getSender().getId(), request.getSender().getProfileImageUrl(), request.getSender().getNickName(), null, null);
        } else if (request.getAlarmType() == AlarmType.NEW_Like_ON_POST) {
            args = new AlarmArgs(request.getSender().getId(), request.getSender().getProfileImageUrl(), request.getSender().getNickName(), request.getPostId(), null);
        } else if (request.getAlarmType() == AlarmType.NEW_COMMENT_ON_POST) {
            args = new AlarmArgs(request.getSender().getId(), request.getSender().getProfileImageUrl(), request.getSender().getNickName(), request.getPostId(), request.getCommentId());
        } else {
            throw new IllegalArgumentException("Unsupported AlarmType: " + request.getAlarmType());
        }


        //Create the alarm and save it to the repository
        Alarm alarm = Alarm.of(request.getReceiverId(), request.getAlarmType(), args);
        alarm = alarmRepository.save(alarm);

        //Send the notification
        send(request.getAlarmType(),alarm.getId(), request.getReceiverId(), args);
    }
    @Transactional
    public Response<Void> deleteAlarm(Long alarmId, String email) {
        //find user
        Member member=memberRepository.findByEmail(email).orElseThrow(() ->
                new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%S not found", email)));

        // alarm existence and authority
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() ->
                new PizzaAppException(ErrorCode.ALARM_NOT_FOUND, "The alarm not found"));
        if (!member.getId().equals(alarm.getReceiverId())) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION, "You are not accessible for this action");
        }
        //delete alarm.
        alarmRepository.deleteById(alarmId);
        return null;
    }
    @Transactional
    public Response<Void> deleteAlarms(String email) {
        //find user
        Member member=memberRepository.findByEmail(email).orElseThrow(() ->
                new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%S not found", email)));

        //delete all the alarms.
        alarmRepository.deleteAllByReceiverId(member.getId());
        return null;
    }

    //TODO 알람 리스트만 받아오면 끝!
    public Page<AlarmDTO> getAlarmList(String email, Pageable pageable) {
        //find user
        Member member=memberRepository.findByEmail(email).orElseThrow(() ->
                new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%S not found", email)));

        return alarmRepository.findAllByReceiverId(pageable,member.getId()).map(AlarmDTO::fromAlarmEntity);
    }
}
