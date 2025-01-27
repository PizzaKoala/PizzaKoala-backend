package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
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

@Tag(name = "좋아요 컨트롤러", description = "좋아요 기능들")
public interface LikeControllerDoc {
    /**
     * like function- 좋아요
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글에 좋아요를 누르셨습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "게시글 좋아요 추가 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS"
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인이 되어있지 않을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "게시글이 존재 하지 않을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 게시글입니다.",
                                    value = """
                                            {
                                              "resultCode": "POST_NOT_FOUND",
                                              "message": "The Post was not found., 10004 not found"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "409", description = "이미 좋아요를 눌렀을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "이미 좋아요를 누르신 게시글입니다.",
                                    value = """
                                              {
                                                "resultCode": "ALREADY_LIKED",
                                                "message": "User has already liked the post., MEEP already liked the post 5"
                                              }
                                            """
                            )))
    })
    @Operation(summary = "게시글 좋아요 기능", description = "게시글에 좋아요 누르는 기능")
    Response<Void> like(@Parameter(
            description = "해당 게시글 아이디 입력 *성공예제 부터 실행해주세요",
            examples = {
                    @ExampleObject(name = "성공예제)좋아요 누르기", value = "1"),
                    @ExampleObject(name = "실패예제)이미 좋아요 누른 경우", value = "5"),
                    @ExampleObject(name = "실패예제)존재하지않는 게시글", value = "10004")
            }
    ) @PathVariable Long postId, Authentication authentication);

    /**
     * unlike function- 좋아요 취소
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글에 좋아요를 누르셨습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "게시글 업로드 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS"
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인이 되어있지 않을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "게시글을 좋아한 적 없거나 게시글이 존재 하지 않을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "존재하지 않는 게시글입니다.",
                                    value = """
                                            {
                                              "resultCode": "POST_NOT_FOUND",
                                              "message": "The Post was not found., 10004 not founded"
                                            }
                                            """
                            ), @ExampleObject(
                                    name = "좋아요한적 없는 게시글입니다.",
                                    value = """
                                            {
                                               "resultCode": "LIKE_NOT_FOUND",
                                               "message": "User hasn't liked the post., MEEP hasn't liked the post 2"
                                             }
                                            """
                            )})),
            @ApiResponse(responseCode = "409", description = "이미 좋아요를 눌렀을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "이미 좋아요를 누르신 게시글입니다.",
                                    value = """
                                              {
                                                "resultCode": "ALREADY_UNLIKED",
                                                "message": "User has already unliked the post., MEEP already unliked the post 1"
                                              }
                                            """
                            )))
    })
    @Operation(summary = "게시글 좋아요 취소 기능", description = "게시글에 좋아요 취소하는 기능")
    Response<Void> unlike(@Parameter(
            description = "해당 게시글 아이디 입력. *성공예제 먼저 실행해 주세요.*",
            examples = {
                    @ExampleObject(name = "성공예제)좋아요 취소하기", value = "1"),
                    @ExampleObject(name = "실패예제)이미 취소한 경우", value = "1"),
                    @ExampleObject(name = "실패예제)좋아요 한적 없는 게시글일 경우", value = "2"),
                    @ExampleObject(name = "실패예제)존재하지않는 게시글", value = "10004")
            }
    ) @PathVariable Long postId, Authentication authentication);

}
