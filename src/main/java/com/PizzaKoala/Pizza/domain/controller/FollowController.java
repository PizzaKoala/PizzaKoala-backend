package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.request.UserJoinRequest;
import com.PizzaKoala.Pizza.domain.controller.request.UserLoginRequest;
import com.PizzaKoala.Pizza.domain.controller.response.AlarmResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.response.UserJoinResponse;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.domain.service.AlarmService;
import com.PizzaKoala.Pizza.domain.service.FollowService;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class FollowController {
    private final FollowService followService;
    @PostMapping("/follow/{following}")
    public Response<Void> followAMember(Authentication authentication, @PathVariable Long following){
        return Response.success(followService.follow(authentication.getName(),following));
    }
    @DeleteMapping("/follow/unfollow/{followingId}")
    public Response<Void> unfollowAMember(Authentication authentication, @PathVariable Long followingId){
        return Response.success(followService.unfollow(authentication.getName(),followingId));
    }


}
