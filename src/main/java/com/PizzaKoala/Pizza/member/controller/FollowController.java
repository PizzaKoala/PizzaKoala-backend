package com.PizzaKoala.Pizza.member.controller;

import com.PizzaKoala.Pizza.member.controller.response.FollowListResponse;
import com.PizzaKoala.Pizza.global.controller.response.Response;
import com.PizzaKoala.Pizza.global.exception.ErrorCode;
import com.PizzaKoala.Pizza.global.exception.PizzaAppException;
import com.PizzaKoala.Pizza.member.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {
    private final FollowService followService;

    /**
     * 유저 팔로우 하기
     */
    @PostMapping("/{following}")
    public Response<Void> followAMember(Authentication authentication, @PathVariable Long following){
        return Response.success(followService.follow(authentication.getName(),following));
    }

    /**
     * 언팔로우 하기
     */
    @DeleteMapping("/{followingId}")
    public Response<Void> unfollowAMember(Authentication authentication, @PathVariable Long followingId){
        return Response.success(followService.unfollow(authentication.getName(),followingId));
    }
    /**
     * 나의 팔로워의 팔로우 끊기
     */
    @DeleteMapping("/follower/{followerId}")
    public Response<Void> deleteAFollower(Authentication authentication, @PathVariable Long followerId){
        return Response.success(followService.deleteAFollower(authentication.getName(),followerId));
    }

    /**
     * 나의 팔로워 & 팔로우 리스트
     * {or} -1 for my followers -2 for users I am following
     */
    @GetMapping("/myList/{or}")
    public Response<Page<FollowListResponse>> followList(Authentication authentication, Pageable pageable, @PathVariable int or){
        if (or>2||or<1) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION,"follower list-1, following list-2");
        }
        return Response.success(followService.myFollowList(authentication.getName(), pageable, or).map(FollowListResponse::fromFollowListDTO));

    }

}
