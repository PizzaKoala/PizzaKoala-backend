package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.response.FollowListResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


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

    @DeleteMapping("/follow/follower/{followerId}")
    public Response<Void> deleteAFollower(Authentication authentication, @PathVariable Long followerId){
        return Response.success(followService.deleteAFollower(authentication.getName(),followerId));
    }
    @GetMapping("/follow/my/{or}") //{or} -1 for my followers -2 for users I am following
    public Response<Page<FollowListResponse>> followList(Authentication authentication, Pageable pageable, @PathVariable int or){
        if (or>2||or<1) {
            throw new PizzaAppException(ErrorCode.INVALID_PERMISSION,"follower list-1, following list-2");
        }
        return Response.success(followService.myFollowList(authentication.getName(), pageable, or).map(FollowListResponse::fromFollowListDTO));

    }

}
