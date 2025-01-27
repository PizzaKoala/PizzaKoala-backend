package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.AlarmRepository;
import com.PizzaKoala.Pizza.domain.Repository.EmitterRepository;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.controller.request.AlarmRequest;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.entity.Alarm;
import com.PizzaKoala.Pizza.domain.entity.AlarmArgs;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.AlarmDTO;
import com.PizzaKoala.Pizza.global.entity.AlarmType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
    private final static Long DEFAULT_TIMEOUT=60L * 1000 *60;
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

//    public AlarmService(MemberRepository memberRepository, EmitterRepository emitterRepository) {
//        this.memberRepository = memberRepository;
//        this.emitterRepository = emitterRepository;
//    }
//    public void send(AlarmType type, AlarmArgs args, Integer receiverId) {
//        UserEntity userEntity = userEntityRepository.findById(receiverId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND));
//        AlarmEntity entity = AlarmEntity.of(type, args, userEntity);
//        alarmEntityRepository.save(entity);
//        emitterRepository.get(receiverId).ifPresentOrElse(it -> {
//                    try {
//                        it.send(SseEmitter.event()
//                                .id(entity.getId().toString())
//                                .name(ALARM_NAME)
//                                .data(new AlarmNoti()));
//                    } catch (IOException exception) {
//                        emitterRepository.delete(receiverId);
//                        throw new SimpleSnsApplicationException(ErrorCode.NOTIFICATION_CONNECT_ERROR);
//                    }
//                },
//                () -> log.info("No emitter founded")
//        );
//    }
//
//    public void send(Long alarmId,Long memberId) {
//        emitterRepository.get(memberId).ifPresentOrElse(sseEmitter -> {
//            try {
//                sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(ALARM_NAME).data("new alarm"));
//            } catch (IOException e) {
//                emitterRepository.delete(memberId);
//                throw new PizzaAppException(ErrorCode.ALARM_CONNECT_ERROR);
//            }
//        }, ()-> log.info("No emitter founded"));
//    }
//
//    public SseEmitter connectAlarm(String email) {
//        Member member=memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded ",email)));
//
//        SseEmitter sseEmitter= new SseEmitter(DEFAULT_TIMEOUT);
//        emitterRepository.save(member.getId(), sseEmitter);
//        // Emitter가 완료될 때(모든 데이터가 성공적으로 전송된 상태) Emitter를 삭제한다.
//        sseEmitter.onCompletion(()->emitterRepository.delete(member.getId()));
//        // Emitter가 타임아웃 되었을 때(지정된 시간동안 어떠한 이벤트도 전송되지 않았을 때) Emitter를 삭제한다.
//        sseEmitter.onTimeout(()->emitterRepository.delete(member.getId()));
//
//        try {
//            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME).data("connect completed"));
//        } catch (IOException exception) {
//            throw new PizzaAppException(ErrorCode.ALARM_CONNECT_ERROR);
//        }
//        return sseEmitter;
//    }

}
