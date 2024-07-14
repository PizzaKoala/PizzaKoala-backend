package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.Response.CommentResponse;
import com.PizzaKoala.Pizza.domain.controller.Response.PostResponse;
import com.PizzaKoala.Pizza.domain.controller.Response.Response;
import com.PizzaKoala.Pizza.domain.controller.request.PostCommentRequest;
import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.request.PostModifyRequest;
import com.PizzaKoala.Pizza.domain.model.PostDTO;
import com.PizzaKoala.Pizza.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {

        postService.create(request.getFile(),authentication.getName(), request.getTitle(), request.getDesc());
//        request.getFile()
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> Modify(@PathVariable Long postId, @RequestBody PostModifyRequest request, Authentication authentication) {

        PostDTO postDTO = postService.modify(authentication.getName(), request.getTitle(), request.getDesc(), postId);
//        request.getFile()
        return Response.success(PostResponse.fromPostDTO(postDTO));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();
    }

    @GetMapping
    public Response<Page<PostResponse>> list(Pageable pageable) {
        return Response.success(postService.list(pageable).map(PostResponse::fromPostDTO));
    }

    @GetMapping("/my")
    public Response<Page<PostResponse>> my(Authentication authentication, Pageable pageable) {
        return Response.success(postService.my(authentication.getName(), pageable).map(PostResponse::fromPostDTO));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable Long postId, Authentication authentication) {
        postService.likes(postId, authentication.getName());
        return Response.success();
    }
    @GetMapping("/{postId}/likes")
    public Response<Long> likeCount(@PathVariable Long postId, Authentication authentication) {
        return Response.success(postService.likeCount(postId));
    }

    @PostMapping("/{postId}/comments")
    public Response<Void> comment(@PathVariable Long postId, @RequestBody PostCommentRequest comment, Authentication authentication) {
        postService.comment(postId,authentication.getName(),comment.getComment());
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> getComments(@PathVariable Long postId, Pageable pageable, Authentication authentication) {
        return Response.success( postService.getComments(postId, pageable).map(CommentResponse::fromCommentDTO));
    }
}
