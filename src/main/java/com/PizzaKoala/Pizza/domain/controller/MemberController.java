package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.response.AlarmResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.response.UserJoinResponse;
import com.PizzaKoala.Pizza.domain.controller.request.UserJoinRequest;
import com.PizzaKoala.Pizza.domain.controller.request.UserLoginRequest;

import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.domain.service.AlarmService;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;
    private final AlarmService alarmService;

    // TODO: implement
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestPart(value = "file") MultipartFile file, @RequestPart("request") UserJoinRequest request, HttpServletResponse response) throws IOException {

        UserDTO joinUserDTO = memberService.join(file, request.getNickName(), request.getEmail(), request.getPassword(), response);
        return Response.success(UserJoinResponse.fromJoinUserDTO(joinUserDTO));

    }


    @PostMapping("/login")
    public Response<Void> login(@RequestBody UserLoginRequest request) {

        memberService.login(request.getEmail(), request.getPassword());

        return Response.success();
    }




    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {

//         authentication 안에 memberId를 넣으면 조회 한번 하는걸 줄일수있다. 시간날떄 변경하기
        return Response.success(memberService.alarmList(authentication.getName(), pageable).map(AlarmResponse::fromAlarmDTO));
    }

    @GetMapping("/alarm/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
       return alarmService.connectAlarm(authentication.getName());

    }
}
