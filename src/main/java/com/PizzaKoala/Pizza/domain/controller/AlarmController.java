package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.response.AlarmResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.service.AlarmService;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/alarm")
public class AlarmController {
    private final AlarmService alarmService;

    @GetMapping
    public SseEmitter subscribe(Authentication authentication) {
        return alarmService.connectAlarm(authentication.getName());

    }

    //TODO authentication 안에 memberId를 넣으면 조회 한번 하는걸 줄일수있다. 시간날떄 변경하기

}
