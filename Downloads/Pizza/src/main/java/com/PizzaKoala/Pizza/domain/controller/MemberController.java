package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.Util.JWTTokenUtils;
import com.PizzaKoala.Pizza.domain.controller.Response.AlarmResponse;
import com.PizzaKoala.Pizza.domain.controller.Response.Response;
import com.PizzaKoala.Pizza.domain.controller.Response.UserJoinResponse;
import com.PizzaKoala.Pizza.domain.controller.Response.UserLoginResponse;
import com.PizzaKoala.Pizza.domain.controller.request.UserJoinRequest;
import com.PizzaKoala.Pizza.domain.controller.request.UserLoginRequest;

import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.model.AlarmDTO;
import com.PizzaKoala.Pizza.domain.model.MemberRole;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    private final JWTTokenUtils jwtTokenUtils;

    // TODO: implement
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserDTO joinUserDTO = memberService.join(request.getNickName(), request.getEmail(), request.getPassword());
        return Response.success(UserJoinResponse.fromJoinUserDTO(joinUserDTO));
    }

//    @PostMapping("/login")
//    public Response<Void> login(@RequestBody UserLoginRequest request) {
//        log.debug("Received login request: {}", request);
//        return Response.success(memberService.login(request.getEmail(), request.getPassword()));
//    }
    @PostMapping("/login")
    public Response<Void> login(@RequestBody UserLoginRequest request) {
        log.debug("Received login request: {}", request);
        memberService.login(request.getEmail(), request.getPassword());

        return Response.success();
    }
//    @PostMapping("/login")
//    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
//        String token = memberService.login(request.getEmail(), request.getPassword());
//
//        return Response.success(new UserLoginResponse(token));
//    }


    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        return Response.success(memberService.alarmList(authentication.getName(), pageable).map(AlarmResponse::fromAlarmDTO));
    }
}
