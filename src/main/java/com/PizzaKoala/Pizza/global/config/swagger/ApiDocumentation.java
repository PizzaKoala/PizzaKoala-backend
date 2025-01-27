package com.PizzaKoala.Pizza.global.config.swagger;


import com.PizzaKoala.Pizza.domain.controller.response.PostListResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.*;

public class ApiDocumentation {
    @Target(ElementType.METHOD)
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글이 업데이트가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "성공 예제) 게시글 삭제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "다른 사람의 게시글 삭제 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "실패 예제) 다른 사람의 게시글은 삭제할 수 없습니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_PERMISSION",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 계정일때 (계정 삭제한 후 게시글 업로드 시도했을때)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제 1) 존재하지 않는 게시글입니다.",
                                    value = """
                                            {
                                              "resultCode": "POST_NOT_FOUND",
                                              "result": "null"
                                            }
                                            """
                            ), @ExampleObject(
                                    name = " 실패 예제 2) 존재하지 않는 계정입니다.",
                                    value = """
                                            {
                                              "resultCode": "MEMBER_NOT_FOUND",
                                              "result": "The user does not exist."
                                            }
                                            """
                            )
                            }))})
    @Operation(summary = "게시글 삭제 기능", description = "내 게시글을 삭제합니다.")
    public @interface DeleteAPost {
    }

    @Target(ElementType.METHOD)
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글이 업데이트가 완료되었습니다.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "게시글 업데이트 성공 예제",
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
            @ApiResponse(responseCode = "401", description = "다른 사람의 게시글 수정 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "실패 예제) 다른 사람의 게시글은 수정할 수 없습니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_PERMISSION",
                                              "result": "null"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시글, 계정일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제 1) 존재하지 않는 게시글입니다.",
                                    value = """
                                            {
                                              "resultCode": "POST_NOT_FOUND",
                                              "result": "null"
                                            }
                                            """
                            ), @ExampleObject(
                                    name = "실패 예제 2) 존재하지 않는 계정입니다.",
                                    value = """
                                            {
                                              "resultCode": "MEMBER_NOT_FOUND",
                                              "result": "The user does not exist."
                                            }
                                            """
                            )
                            })),
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
                            )))})
    @Operation(summary = "게시글 수정 기능", description = "사진과 함께 게시글을 해주세요. 사진은 1~5개까지 올려주세요. *사진을 하나 이상 올려야 실행됩니다.*")
    public @interface ModifyAPost {
    }

    @Target(ElementType.METHOD)
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 단건 조회- with 모든 댓글(사용자가 많아지면 댓글은 페이지네이션으로 처리하기).",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "게시글 단건 조회 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS",
                                               "result": {
                                                 "게시글 쓴 유저 아이디": 1,
                                                 "게시글 쓴 유저 프로필 사진 url": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-12-21-27-13-2.jpeg",
                                                 "게시글 쓴 유저 닉네임": "MEEP",
                                                 "게시글 아이디": 3,
                                                 "제목": "박물관 다녀온 날",
                                                 "게시글 내용": "박물관 굿즈들 구경하는 재미가 있다. 아기 용가리들도 너무 귀여웠다. ",
                                                 "좋아요 수": 1,
                                                 "사진 url": [
                                                   "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-57.jpeg"
                                                 ],
                                                 "생성일자": "2025-01-21 20:27:13",
                                                 "수정일자": "2025-01-21 20:27:14",
                                                 "comments": [
                                                   {
                                                     "댓글 쓴 유저 아이디": 1,
                                                     "유저 닉네임": "MEEP",
                                                     "유저 프로필 사진": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-12-21-27-13-2.jpeg",
                                                     "댓글 아이디": 5,
                                                     "댓글 내용": "앙뇽",
                                                     "게시글 아이디": 3,
                                                     "생성날짜": "2025-01-21 23:48:57",
                                                     "수정날짜": "2025-01-21 23:48:57"
                                                   }
                                                 ]
                                               }
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "삭제된 게시글 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "실패 예제) 존재하지 않는 게시글입니다.",
                                    value = """
                                            {
                                              "resultCode": "POST_NOT_FOUND",
                                              "result": "null"
                                            }
                                            """
                            )
                            }))})
    @Operation(summary = "게시글 단건 조회", description = "게시글 아이디로 단건 조회합니다")
    public @interface GetAPost {
    }
    @Target(ElementType.METHOD)
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "메인 페이지 좋아요 순 게시글들 - 로그인 전",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "게시글 단건 조회 성공 예제",
                                    value = """
                                            {
                                                "resultCode": "SUCCESS",
                                                "result": {
                                                  "content": [
                                                    {
                                                      "게시글 아이디": 1, //(좋아요 3개)
                                                      "게시글 제목": "What a meepy day.",
                                                      "게시글 사진 url": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/47896696-f511-4697-b3f5-2a7b194a3dbc.png",
                                                      "사진 갯수": 3
                                                    },
                                                    {
                                                      "게시글 아이디": 5, //(좋아요 3개)
                                                      "게시글 제목": "파스스",
                                                      "게시글 사진 url": null,
                                                      "사진 갯수": 0
                                                    },
                                                    {
                                                      "게시글 아이디": 2, //(좋아요 2개)
                                                      "게시글 제목": "애민이",
                                                      "게시글 사진 url": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-06-17-22-15-23.jpeg",
                                                      "사진 갯수": 1
                                                    },
                                                    {
                                                      "게시글 아이디": 3, //(좋아요 1개)
                                                      "게시글 제목": "박물관 다녀온 날",
                                                      "게시글 사진 url": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-57.jpeg",
                                                      "사진 갯수": 1
                                                    },
                                                    {
                                                      "게시글 아이디": 4, //(좋아요 0개)
                                                      "게시글 제목": "점심",
                                                      "게시글 사진 url": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-43.jpeg",
                                                      "사진 갯수": 1
                                                    }
                                                  ],
                                                  "pageable": {
                                                    "pageNumber": 0,
                                                    "pageSize": 20,
                                                    "sort": {
                                                      "empty": true,
                                                      "sorted": false,
                                                      "unsorted": true
                                                    },
                                                    "offset": 0,
                                                    "paged": true,
                                                    "unpaged": false
                                                  },
                                                  "totalPages": 1,
                                                  "totalElements": 5,
                                                  "last": true,
                                                  "size": 20,
                                                  "number": 0,
                                                  "sort": {
                                                    "empty": true,
                                                    "sorted": false,
                                                    "unsorted": true
                                                  },
                                                  "numberOfElements": 5,
                                                  "first": true,
                                                  "empty": false
                                                }
                                              }
                                            """
                            )))})
    @Operation(summary = "메인 페이지 조회) 좋아요 순 게시글들", description = "로그인 없이 조회 가능합니다. 20개씩 게시글을 가져옵니다. 페이지번호 0부터 시작")
    public @interface GetMainPostsMostLiked {
    }



    @Target(ElementType.PARAMETER) // 애노테이션을 매개변수에 적용
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(
            description = "해당 게시글 아이디 입력",
            examples = {
                    @ExampleObject(name = "내 게시글 - 이미 삭제되었을 경우 내 게시글 아이디를 직접 입력해주세요.", value = "6"),
                    @ExampleObject(name = "내 게시글이 아닌 게시글", value = "4"),
                    @ExampleObject(name = "존재하지않는 게시글", value = "-1")
            }
    )
    public @interface MyPostIdParameter {
    }

    @Target(ElementType.PARAMETER) // 애노테이션을 매개변수에 적용
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Parameter(
            description = "해당 게시글 아이디 입력",
            examples = {
                    @ExampleObject(name = "성공예제) 게시글 검색", value = "3"),
                    @ExampleObject(name = "실패 예제) 존재하지않는 게시글", value = "-1")
            }
    )
    public @interface PostIdParameter {
    }



}