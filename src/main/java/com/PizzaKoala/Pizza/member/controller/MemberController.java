package com.PizzaKoala.Pizza.member.controller;

import com.PizzaKoala.Pizza.global.controller.response.Response;
import com.PizzaKoala.Pizza.member.controller.response.UserJoinResponse;
import com.PizzaKoala.Pizza.member.controller.request.UserJoinRequest;
import com.PizzaKoala.Pizza.member.controller.request.UserLoginRequest;

import com.PizzaKoala.Pizza.member.controller.response.UserResponse;
import com.PizzaKoala.Pizza.member.dto.UserDTO;
import com.PizzaKoala.Pizza.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;


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

    @GetMapping("/me")
    public Response<UserResponse> mydetail(Authentication authentication) {
       UserDTO userDTO= memberService.getMyDetail(authentication);
        return Response.success(UserResponse.fromUser(userDTO));
    }
}
