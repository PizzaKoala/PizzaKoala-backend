package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.EmitterRepository;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@Slf4j
@AllArgsConstructor
public class AlarmService {
    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;
    private final static Long DEFAULT_TIMEOUT=60L * 1000 *60;
    private final static String ALARM_NAME = "alarm";

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

    public void send(Long alarmId,Long memberId) {
        emitterRepository.get(memberId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(ALARM_NAME).data("new alarm"));
            } catch (IOException e) {
                emitterRepository.delete(memberId);
                throw new PizzaAppException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, ()-> log.info("No emitter founded"));
    }

    public SseEmitter connectAlarm(String email) {
        Member member=memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded ",email)));

        SseEmitter sseEmitter= new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(member.getId(), sseEmitter);
        sseEmitter.onCompletion(()->emitterRepository.delete(member.getId()));
        sseEmitter.onTimeout(()->emitterRepository.delete(member.getId()));

        try {
            sseEmitter.send(SseEmitter.event().id("").name(ALARM_NAME).data("connect completed"));
        } catch (IOException exception) {
            throw new PizzaAppException(ErrorCode.ALARM_CONNECT_ERROR);
        }
        return sseEmitter;
    }

}
