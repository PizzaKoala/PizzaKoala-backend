package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.Util.JWTTokenUtils;
import com.PizzaKoala.Pizza.domain.controller.Response.AlarmResponse;
import com.PizzaKoala.Pizza.domain.controller.Response.Response;
import com.PizzaKoala.Pizza.domain.controller.Response.UserJoinResponse;
import com.PizzaKoala.Pizza.domain.controller.Response.UserLoginResponse;
import com.PizzaKoala.Pizza.domain.controller.request.UserJoinRequest;
import com.PizzaKoala.Pizza.domain.controller.request.UserLoginRequest;

import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.AlarmDTO;
import com.PizzaKoala.Pizza.domain.model.MemberRole;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    private final JWTTokenUtils jwtTokenUtils;

    // TODO: implement
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestPart(value = "file") MultipartFile file,@RequestPart ("request") UserJoinRequest request, HttpServletResponse response) throws IOException {

            UserDTO joinUserDTO = memberService.join(file, request.getNickName(), request.getEmail(), request.getPassword(),response);
            return Response.success(UserJoinResponse.fromJoinUserDTO(joinUserDTO));

    }


    @PostMapping("/login")
    public Response<Void> login(@RequestBody UserLoginRequest request) {

        memberService.login(request.getEmail(), request.getPassword());

        return Response.success();
    }



    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        return Response.success(memberService.alarmList(authentication.getName(), pageable).map(AlarmResponse::fromAlarmDTO));
    }
}
