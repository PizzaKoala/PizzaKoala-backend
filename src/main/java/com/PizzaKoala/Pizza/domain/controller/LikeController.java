package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.swagInterface.LikeControllerDoc;
import com.PizzaKoala.Pizza.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/like")
@RequiredArgsConstructor
public class LikeController implements LikeControllerDoc {

    private final PostService postService;

    /**
     * like function- 좋아요
     * 라이크 취소시 db에서 지워지지 않고 그냥 deletedAt으로 추가됨. 실수할수있으니 그냥 지울걸 그랬나.. 고민해보자
     * 지금은 자기 게시글을 좋아요 할수있음 나중에는 자기글은 좋아요 못누르게 하장..
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
