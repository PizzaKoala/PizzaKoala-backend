package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.request.PostModifyRequest;
import com.PizzaKoala.Pizza.domain.controller.response.PostListResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.swagInterface.PostControllerDoc;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.PostWithCommentsDTO;
import com.PizzaKoala.Pizza.domain.service.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 왜 안되노
 * </p>
 */

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController implements PostControllerDoc {

    private final PostService postService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<Void> create(@RequestPart("files") List<MultipartFile> files,
                                 @RequestPart("request") PostCreateRequest request, Authentication authentication) {
        //        image's quantity check
        if (files.isEmpty() || files.size() > 5) {
            throw new PizzaAppException(ErrorCode.ONE_TO_FIVE_IMAGES_ARE_REQUIRED);
        }
        postService.create(files, authentication.getName(), request.getTitle(), request.getDesc());
        return Response.success();
    }

    /**
     * update a post - TODO: 이건 리턴 아무것도 안하고 그냥 프론트측에서 단건 포스트 조회 하는게 좋을까..?! 받아오는김에 다 전해줄지..
     * <p>
     * -시간되면 수정하기-
     * 사진 다 삭제하는것보다 삭제 요청받은 사진들만 삭제하고 추가 요청받은 사진들은 요청하고
     * 사진은 변경이 없다면 그냥 건들지 않는 로직으로 변경하는게 좋겠다.
     */
    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<Void> Modify(@PathVariable Long postId, @RequestPart List<MultipartFile> files, @RequestPart PostModifyRequest request, Authentication authentication) throws IOException {
        postService.modify(authentication.getName(), files, request.getTitle(), request.getDesc(), postId);
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
     * 나중에 사용자가 많을때 페이지네이션 고려해보기
     * 컨트롤러 안에 댓글이 60개보다 많을때 페이지네이션 되게만들어도 될듯..
     * 일단 60개보다 더 있다면 댓글 가져오는 comment api with api 만들어서 60개 이후 댓글 가져오는거 만들기
     * 비동기로 한다던데 필요할때 알아보기
     */
    @GetMapping("/{postId}")
    public Response<PostWithCommentsDTO> getAPost(@PathVariable Long postId) {
        PostWithCommentsDTO postWithCommentsDTOS = postService.getAPost(postId);
        return Response.success(postWithCommentsDTOS);
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

    /**
     * 메인 패이지- 팔로잉 맴버들의 포스트들
     */
    @GetMapping("/main/following")
    public Response<Page<PostListResponse>> FollowingList(Pageable pageable, Authentication authentication) {
        return Response.success(postService.followingPosts(pageable, authentication.getName()).map(PostListResponse::fromPostImageDTO));
    }

    /**
     * 메인 패이지- 좋아요 순 포스트들
     * 로그인 없이 접속 가능한 페이지
     */
    @GetMapping("/main/likes")
    public Response<Page<PostListResponse>> LikedList(Pageable  pageable) {
        return Response.success(postService.LikedList(pageable).map(PostListResponse::fromPostImageDTO));
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
