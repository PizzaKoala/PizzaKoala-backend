package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.request.PostCommentRequest;
import com.PizzaKoala.Pizza.global.controller.response.Response;
import com.PizzaKoala.Pizza.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;

    /**
     * leave a comment - 댓글 달기
     */
    @PostMapping("/{postId}")
    public Response<Void> comment(@PathVariable Long postId, @RequestBody PostCommentRequest comment, Authentication authentication) {
        postService.comment(postId,authentication.getName(),comment.getComment());
        return Response.success();
    }




    /**
     *  delete a comment - 댓글 삭제
     */
    @DeleteMapping("/{postId}")
    public Response<Boolean> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication) {
        return Response.success(postService.commentDelete(postId,commentId,authentication.getName()));
    }





    /**
     *  edit a comment - 댓글 수정
     */
    @PutMapping("/{postId}")
    public Response<Boolean> editComment(@PathVariable Long postId , @RequestBody PostCommentRequest editedComment, Authentication authentication) {
        return Response.success(postService.editComment(postId,editedComment,authentication.getName()));
    }

}
