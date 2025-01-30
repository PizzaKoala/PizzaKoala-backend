package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.response.AlarmResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.swagInterface.AlarmControllerDoc;
import com.PizzaKoala.Pizza.domain.service.AlarmService;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/alarm")
public class AlarmController implements AlarmControllerDoc {
    private final AlarmService alarmService;
    private final MemberService memberService;


    @GetMapping("/list")
    public Response<Page<AlarmResponse>> alarmLists(Authentication authentication, Pageable pageable) {
        return Response.success(alarmService.getAlarmList(authentication.getName(),pageable).map(AlarmResponse::fromAlarmDTO));
    }
    @GetMapping
    public SseEmitter subscribe(Authentication authentication) {
        return alarmService.connectAlarm(authentication.getName());
    }

//    @GetMapping("/list")
//    public Response<Page<AlarmResponse>> alarmLists(Authentication authentication, Pageable pageable) {
//
//        return Response.success(alarmService.getAlarmList(authentication.getName(),pageable).map(AlarmResponse::fromAlarmDTO));
//    }

    //TODO authentication 안에 memberId를 넣으면 조회 한번 하는걸 줄일수있다. 시간날떄 변경하기

    @DeleteMapping("/delete/{alarmId}")
    public Response<Void> deleteAlarm(@PathVariable Long alarmId, Authentication authentication) {
        return alarmService.deleteAlarm(alarmId, authentication.getName());
    }
    @DeleteMapping("/delete/all")
    public Response<Void> deleteAllAlarms(Authentication authentication) {
        return alarmService.deleteAlarms(authentication.getName());
    }


}
