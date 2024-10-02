package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController {

    private final PostService postService;

    /**
     * like function- 좋아요
     */

    @PostMapping("/{postId}")
    public Response<Void> like(@PathVariable Long postId, Authentication authentication) {
        postService.likes(postId, authentication.getName());
        return Response.success();
    }
    /**
     * unlike function- 좋아요 취소
     */

    @PostMapping("/{postId}/unlike")
    public Response<Void> unlike(@PathVariable Long postId, Authentication authentication) {
        postService.unlikes(postId, authentication.getName());
        return Response.success();
    }


//    /**
//     * like- 포스트의 like 수 가져오기- 이건 어캐 사용하지
//     */
//    @GetMapping("/{postId}/likes")
//    public Response<Long> likeCount(@PathVariable Long postId, Authentication authentication) {
//        return Response.success(postService.likeCount(postId));
//    }
}
