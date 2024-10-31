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
     * -그냥 아무것도 리턴 안하기로 함.
     */
    @PutMapping("/{postId}")
    public Response<Void> Modify(@PathVariable Long postId, @RequestPart List<MultipartFile> files, @RequestPart PostModifyRequest request, Authentication authentication) throws IOException {
        postService.modify(authentication.getName(),files, request.getTitle(), request.getDesc(), postId);
        return Response.success();
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
    @GetMapping("/{postId}")
    public Response<Page<PostWithCommentsDTO>> getAPost(@PathVariable Long postId,Pageable pageable) {
        Page<PostWithCommentsDTO> postWithCommentsDTOS = postService.getAPost(postId, pageable);
        return Response.success(postWithCommentsDTOS);
    }



    /**
     * 메인 패이지- 팔로잉 맴버들의 포스트들
     */
    @GetMapping("/main/following")
    public Response<Page<PostListResponse>> FollowingList(Pageable pageable,Authentication authentication) {
        return Response.success(postService.followingPosts(pageable,authentication.getName()).map(PostListResponse::fromPostImageDTO));
    }

    /**
     * 메인 패이지- 좋아요 순 포스트들
     */
    @GetMapping("/main/likes")
    public Response<Page<PostListResponse>> LikedList(Pageable pageable) {
        return Response.success(postService.LikedList(pageable).map(PostListResponse::fromPostImageDTO));
    }





    /**
     * my posts- 내 포스트들 리스트로 끌고 오기/ 메인 게시물
     */

    @GetMapping("/myList")
    public Response<Page<PostListResponse>> myPosts(Authentication authentication, Pageable pageable) {
                return Response.success(postService.my(authentication.getName(), pageable).map(PostListResponse::fromPostImageDTO));
    }

    /**
     * member posts-특정 맴버 포스트들 리스트로 끌고 오기
     */
    @GetMapping("/user/{memberId}")
    public Response<Page<PostListResponse>> memberPosts(@PathVariable Long memberId, Pageable pageable) {
                return Response.success(postService.memberPosts(memberId, pageable).map(PostListResponse::fromPostImageDTO));
    }






//    /**
//     * get post comments - 특정 포스트의 달린 댓글 가져오기
//     */
//    @GetMapping("/{postId}/comments")
//    public Response<Page<CommentResponse>> getComments(@PathVariable Long postId, Pageable pageable) {
//        return Response.success( postService.getComments(postId, pageable).map(CommentResponse::fromCommentDTO));
//    }

//    /**
//     * recent posts 최신 포스트들
//     */
//
//    @GetMapping
//    public Response<Page<PostListResponse>> recentList(Pageable pageable) {
//        return Response.success(postService.list(pageable).map(PostListResponse::fromPostImageDTO));
//    }

//    /**
//     * update a post -
//     */
//    @PutMapping("/{postId}")
//    public Response<PostResponse> Modify(@PathVariable Long postId, @RequestPart List<MultipartFile> files, @RequestPart PostModifyRequest request, Authentication authentication) throws IOException {
//
//        PostDTO postDTO = postService.modify(authentication.getName(),files, request.getTitle(), request.getDesc(), postId);
////        request.getFile()
//        return Response.success(PostResponse.fromPostDTO(postDTO));
//    }


}
