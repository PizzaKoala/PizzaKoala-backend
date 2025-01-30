package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import com.PizzaKoala.Pizza.domain.controller.request.PostCommentCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.request.PostCommentModifyRequest;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "댓글쓰기APIs")
public interface CommentControllerDoc {
    /**
     * leave a comment - 댓글 달기
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글에 댓글을 남깁니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCommentCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "댓글남기기 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS"
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "존재하지 않는 게시글이거나 로그인을 안했을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {
                                    @ExampleObject(
                                            name = "존재하지 않는 게시글입니다",
                                            value = """
                                                            {
                                                              "resultCode": "POST_NOT_FOUND",
                                                              "message": "The Post was not found., -1 not found"
                                                            }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "로그인해야 사용할수있는 기능입니다",
                                            value = """
                                                    {
                                                      "resultCode": "INVALID_TOKEN",
                                                      "message": "Full authentication is required to access this resource"
                                                    }
                                                    """
                                    )
                            }))
    })
    @Operation(summary = "게시글에 댓글 남기기", description = "게시글에 댓글을 남겨주세요 (게시글 아이디 입력), 대댓글은 추가로 넣을 예정")
    Response<Void> comment(@Parameter(
            description = "해당 게시글 아이디 입력 *성공예제 부터 실행해주세요",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "1"),
                    @ExampleObject(name = "예시2-게시글이 삭제되거나 존재하지 않는 경우", value = "50")
            }
    ) @PathVariable Long postId, @RequestBody PostCommentCreateRequest comment, Authentication authentication);

    /**
     * delete a comment - 댓글 삭제
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글을 삭제합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCommentCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "댓글삭제하기 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS"
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "내 댓글이 아니거나 로그인을 안했을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {
                                    @ExampleObject(
                                            name = "자신이 쓴 댓글만 삭제 가능합니다.",
                                            value = """
                                                            {
                                                              "resultCode": "INVALID_PERMISSION",
                                                              "message": "Permission is invalid."
                                                            }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "로그인해야 사용할수있는 기능입니다",
                                            value = """
                                                    {
                                                      "resultCode": "INVALID_TOKEN",
                                                      "message": "Full authentication is required to access this resource"
                                                    }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCommentCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 댓글입니다",
                                    value = """
                                            {
                                              "resultCode": "COMMENT_NOT_FOUND",
                                              "message": "The comment does not exist."
                                            }
                                            """
                            ))),
    })
    @Operation(summary = "댓글 삭제하기", description = "댓글 삭제하는 기능입니다 ")
    Response<Boolean> deleteComment(
            @Parameter(
                    description = "게시글과 댓글 아이디 입력 *성공예제부터 실행해주세요*",
                    examples = {
                            @ExampleObject(name = "예시1-성공", value = "4"),
                            @ExampleObject(name = "예시2-내가 쓴 댓글이 아닌 경우", value = "1"),
                            @ExampleObject(name = "예시4-댓글이 삭제되거나 존재하지 않는 경우", value = "2")
                    }
            )
            @PathVariable Long postId,

            @Parameter(
                    description = "댓글 ID",
                    examples = {
                            @ExampleObject(name = "예시1-성공", value = "1"),
                            @ExampleObject(name = "예시2-내가 쓴 댓글이 아닌 경우", value = "2"),
                            @ExampleObject(name = "예시3-댓글이 삭제되거나 존재하지 않는 경우", value = "10")
                    }
            )@PathVariable Long commentId, Authentication authentication);


    /**
     * edit a comment - 댓글 수정
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글을 삭제합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCommentCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "댓글삭제하기 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": true
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "내 댓글이 아니거나 로그인을 안했을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {
                                    @ExampleObject(
                                            name = "로그인해야 사용할수있는 기능입니다",
                                            value = """
                                                    {
                                                      "resultCode": "INVALID_TOKEN",
                                                      "message": "Full authentication is required to access this resource"
                                                    }
                                                    """
                                    )
                            })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCommentCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 댓글입니다",
                                    value = """
                                            {
                                              "resultCode": "COMMENT_NOT_FOUND",
                                              "message": "The comment does not exist."
                                            }
                                            """
                            ))),
    })
    @Operation(summary = "댓글 수정하기", description = "댓글을 수정해주세요 ")
    Response<Boolean> editComment(@Parameter(
            description = "해당 게시글 아이디 입력 *성공예제 부터 실행해주세요*",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "1"),
                    @ExampleObject(name = "예시2-댓글이 삭제되거나 존재하지 않는 경우", value = "50")
            }
    ) @PathVariable Long postId, @RequestBody PostCommentModifyRequest editedComment, Authentication authentication);
}
