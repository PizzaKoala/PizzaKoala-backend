package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.response.CommentResponse;
import com.PizzaKoala.Pizza.domain.controller.response.PostListResponse;
import com.PizzaKoala.Pizza.domain.controller.response.PostResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.request.PostCommentRequest;
import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.request.PostModifyRequest;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.CustomUserDetailsDTO;
import com.PizzaKoala.Pizza.domain.model.PostDTO;
import com.PizzaKoala.Pizza.domain.model.PostWithCommentsDTO;
import com.PizzaKoala.Pizza.domain.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    /**
     * create a post
     */
    @PostMapping
    public Response<Void> create(@RequestPart PostCreateRequest request, @RequestPart(required = false) List<MultipartFile> files, Authentication authentication) throws IOException {
        //        image's quantity check
        //TODO: 5이상은 막는데 0개일떄는 못막음 도와줘 :(
        if (files.isEmpty() ||files.size() > 5) {
            throw new PizzaAppException(ErrorCode.ONE_TO_FIVE_IMAGES_ARE_REQUIRED);
        }
        CustomUserDetailsDTO member = (CustomUserDetailsDTO) authentication.getPrincipal();
        System.out.println("authentication = " + member.getUsername());
        postService.create(files,authentication.getName(), request.getTitle(), request.getDesc());
        return Response.success();
    }
    /**
     * update a post - TODO: 이건 리턴 아무것도 안하고 그냥 프론트측에서 단건 포스트 조회 하는게 좋을까..?! 받아오는김에 다 전해줄지..
     */
    @PutMapping("/{postId}")
    public Response<PostResponse> Modify(@PathVariable Long postId, @RequestPart List<MultipartFile> files, @RequestPart PostModifyRequest request, Authentication authentication) throws IOException {

        PostDTO postDTO = postService.modify(authentication.getName(),files, request.getTitle(), request.getDesc(), postId);
//        request.getFile()
        return Response.success(PostResponse.fromPostDTO(postDTO));
    }

    /**
     * delete a post- 포스트 단건 삭제(soft delete)
     */
    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);
        return Response.success();
    }

    /**
     * get a post- 포스트 단건 조회
     */
    @GetMapping("/with_comments/{postId}")
    public Response<Page<PostWithCommentsDTO>> getAPost(@PathVariable Long postId,Pageable pageable) {
        Page<PostWithCommentsDTO> postWithCommentsDTOS = postService.getAPost(postId, pageable);
        return Response.success(postWithCommentsDTOS);
    }
    /**
     * get post comments - 특정 포스트의 달린 댓글 가져오기
     */
    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> getComments(@PathVariable Long postId, Pageable pageable, Authentication authentication) {
        return Response.success( postService.getComments(postId, pageable).map(CommentResponse::fromCommentDTO));
    }

    /**
     * recent posts 최신 포스트들
     */

    @GetMapping
    public Response<Page<PostListResponse>> recentList(Pageable pageable) {
        return Response.success(postService.list(pageable).map(PostListResponse::fromPostImageDTO));
    }

    /**
     * 좋아요 순 포스트들
     */

    @GetMapping("/liked")
    public Response<Page<PostListResponse>> LikedList(Pageable pageable) {
        return Response.success(postService.LikedList(pageable).map(PostListResponse::fromPostImageDTO));
    }


    /**
     * my posts- 내 포스트들 리스트로 끌고 오기/ 메인 게시물
     */

    @GetMapping("/my")
    public Response<Page<PostListResponse>> my(Authentication authentication, Pageable pageable) {
                return Response.success(postService.my(authentication.getName(), pageable).map(PostListResponse::fromPostImageDTO));
    }
    /**
     * member posts-특정 맴버 포스트들 리스트로 끌고 오기
     */

    @GetMapping("/{postId}")
    public Response<Page<PostListResponse>> memberPosts(@PathVariable Long postId, Pageable pageable) {
                return Response.success(postService.memberPosts(postId, pageable).map(PostListResponse::fromPostImageDTO));
    }


    /**
     * like function- 좋아요
     */

    @PostMapping("/{postId}/like")
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


    /**
     * like- 포스트의 like 수 가져오기- 이건 어캐 사용하지
     */
    @GetMapping("/{postId}/likes")
    public Response<Long> likeCount(@PathVariable Long postId, Authentication authentication) {
        return Response.success(postService.likeCount(postId));
    }

    /**
     * leave a comment - 댓글 달기
     */
    @PostMapping("/{postId}/comments")
    public Response<Void> comment(@PathVariable Long postId, @RequestBody PostCommentRequest comment, Authentication authentication) {
        postService.comment(postId,authentication.getName(),comment.getComment());
        return Response.success();
    }

    /**
     *  delete a comment - 댓글 삭제
     */
    @DeleteMapping("/{postId}/{commentId}")
    public Response<Boolean> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Authentication authentication) {
        return Response.success(postService.commentDelete(postId,commentId,authentication.getName()));
    }


}
