package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.EmitterRepository;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@Slf4j
public class AlarmService {
    private final MemberService memberService;
    private final EmitterRepository emitterRepository;
    private final static Long DEFAULT_TIMEOUT=60L * 1000 *60;
    private final static String ALARM_NAME = "alarm";

    public AlarmService(MemberService memberService, EmitterRepository emitterRepository) {
        this.memberService = memberService;
        this.emitterRepository = emitterRepository;
    }

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
        Member member=memberService.loadMemberByEmail(email);
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
