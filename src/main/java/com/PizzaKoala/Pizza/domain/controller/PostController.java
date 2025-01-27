package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.request.PostModifyRequest;
import com.PizzaKoala.Pizza.domain.controller.response.PostListResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.PostWithCommentsDTO;
import com.PizzaKoala.Pizza.domain.service.PostService;
import com.PizzaKoala.Pizza.global.config.swagger.ApiDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@Tag(name = "게시글 컨트롤러", description = "게시글에 관한 기능들")
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글이 완료되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "게시글 업로드 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "400", description = "사진은 1~5개만 올릴 수 있습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "실패 예제) 사진이 없거나 5장 초과일때",
                                    value = """
                                            {
                                              "resultCode": "ONE_TO_FIVE_IMAGES_ARE_REQUIRED",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "토큰 안에 있는 계정이 존재하지 않을때 (계정 삭제한 후 게시글 업로드 시도했을때)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 계정입니다.",
                                    value = """
                                            {
                                              "resultCode": "MEMBER_NOT_FOUND",
                                              "result": "The user does not exist."
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "500", description = "사진 업로드(s3포함) 과정에서 업로드 실패했을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "서버 문제로 업로드에 실패했습니다. 잠시후 다시 업로드 해주세요",
                                    value = """
                                            {
                                              "resultCode": "S3_UPLOAD_FAILED",
                                              "result": "Error occurred while uploading to S3."
                                            }
                                            """
                            )))
    })
    @Operation(summary = "게시글 올리기 with 사진", description = "사진과 함께 게시글을 해주세요. 사진은 1~5개까지 올려주세요. *사진을 하나 이상 올려야 실행됩니다.*")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<Void> create(@RequestPart("files") List<MultipartFile> files,
                                 @RequestPart("request") PostCreateRequest request, Authentication authentication) throws IOException {
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
    @ApiDocumentation.ModifyAPost
    @PutMapping(value = "/{postId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<Void> Modify(@ApiDocumentation.MyPostIdParameter @PathVariable Long postId, @RequestPart List<MultipartFile> files, @RequestPart PostModifyRequest request, Authentication authentication) throws IOException {
        postService.modify(authentication.getName(), files, request.getTitle(), request.getDesc(), postId);
        return Response.success();
    }

    /**
     * delete a post- 포스트 단건 삭제(soft delete)
     */
    @ApiDocumentation.DeleteAPost
    @DeleteMapping("/{postId}")
    public Response<Void> delete(@ApiDocumentation.MyPostIdParameter
                                 @PathVariable Long postId, Authentication authentication) {
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
    @ApiDocumentation.GetAPost
    @GetMapping("/{postId}")
    public Response<PostWithCommentsDTO> getAPost(@ApiDocumentation.PostIdParameter @PathVariable Long postId) {
        PostWithCommentsDTO postWithCommentsDTOS = postService.getAPost(postId);
        return Response.success(postWithCommentsDTOS);
    }

    /**
     * my posts- 내 포스트들 리스트로 끌고 오기/ 메인 게시물
     */

    @GetMapping("/myList")
    public Response<Page<PostListResponse>> myPosts(Authentication authentication, @ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable pageable) {
        return Response.success(postService.my(authentication.getName(), pageable).map(PostListResponse::fromPostImageDTO));
    }

    /**
     * member posts-특정 맴버 포스트들 리스트로 끌고 오기
     */
    @GetMapping("/user/{memberId}")
    public Response<Page<PostListResponse>> memberPosts(@PathVariable Long memberId, @ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable pageable) {
        return Response.success(postService.memberPosts(memberId, pageable).map(PostListResponse::fromPostImageDTO));
    }

    /**
     * 메인 패이지- 팔로잉 맴버들의 포스트들
     */
    @GetMapping("/main/following")
    public Response<Page<PostListResponse>> FollowingList(@ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable pageable, Authentication authentication) {
        return Response.success(postService.followingPosts(pageable, authentication.getName()).map(PostListResponse::fromPostImageDTO));
    }

    /**
     * 메인 패이지- 좋아요 순 포스트들
     * 로그인 없이 접속 가능한 페이지
     */
    @ApiDocumentation.GetMainPostsMostLiked
    @GetMapping("/main/likes")
    public Response<Page<PostListResponse>> LikedList(@ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable  pageable) {
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
