package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.response.PostListResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.response.SearchNicknamesResponse;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "검색APIs")
public interface SearchControllerDoc {
    /**
     * 최신순- 포스트 검색
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "키워드에 포함된 게시글을 최신순으로 조회합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "키워드 검색 게시글 최신순 조회 성공 예제",
                                    value = """
                                            {
                                                  "resultCode": "SUCCESS",
                                                  "result": {
                                                    "content": [
                                                      {
                                                        "id": 5,
                                                        "title": "파스스",
                                                        "imageUrl": null,
                                                        "imageCount": 0
                                                      },
                                                      {
                                                        "id": 4,
                                                        "title": "점심",
                                                        "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-43.jpeg",
                                                        "imageCount": 1
                                                      },
                                                      {
                                                        "id": 1,
                                                        "title": "What a meepy day.",
                                                        "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/47896696-f511-4697-b3f5-2a7b194a3dbc.png",
                                                        "imageCount": 3
                                                      }
                                                    ],
                                                    "pageable": {
                                                      "pageNumber": 0,
                                                      "pageSize": 20,
                                                      "sort": {
                                                        "empty": true,
                                                        "unsorted": true,
                                                        "sorted": false
                                                      },
                                                      "offset": 0,
                                                      "paged": true,
                                                      "unpaged": false
                                                    },
                                                    "totalElements": 3,
                                                    "totalPages": 1,
                                                    "last": true,
                                                    "size": 20,
                                                    "number": 0,
                                                    "sort": {
                                                      "empty": true,
                                                      "unsorted": true,
                                                      "sorted": false
                                                    },
                                                    "numberOfElements": 3,
                                                    "first": true,
                                                    "empty": false
                                                  }
                                                }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인을 안한 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )))
    })
    @Operation(summary = "게시글 검색 기능- 최신순", description = "최근 순으로 게시글이 검색됩니다.")
    Response<Page<PostListResponse>> searchPostByRecent(@Parameter(
            description = "게시글 검색을 위한 키워드 입력.",
            examples = {
                    @ExampleObject(name = "예시1-성공 1", value = "m"),
                    @ExampleObject(name = "예시2-성공 2", value = "meep")}
    ) @PathVariable String keyword, @ParameterObject @PageableDefault(page = 0, size = 20) Pageable pageable);

    /**
     * 요건 로그인 없이 검색 가능
     * 좋아요순(추천순) 포스트 검색
     */
            @ApiResponse(responseCode = "200", description = "키워드에 포함된 게시글을 좋아요가 가장 많은 순서로 조회합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "키워드 검색 게시글 좋아요순 조회 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": {
                                                "content": [
                                                  {
                                                    "id": 1,
                                                    "title": "What a meepy day.",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/47896696-f511-4697-b3f5-2a7b194a3dbc.png",
                                                    "imageCount": 3
                                                  },
                                                  {
                                                    "id": 5,
                                                    "title": "파스스",
                                                    "imageUrl": null,
                                                    "imageCount": 0
                                                  },
                                                  {
                                                    "id": 4,
                                                    "title": "점심",
                                                    "imageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-43.jpeg",
                                                    "imageCount": 1
                                                  }
                                                ],
                                                "pageable": {
                                                  "pageNumber": 0,
                                                  "pageSize": 20,
                                                  "sort": {
                                                    "empty": true,
                                                    "unsorted": true,
                                                    "sorted": false
                                                  },
                                                  "offset": 0,
                                                  "paged": true,
                                                  "unpaged": false
                                                },
                                                "totalElements": 3,
                                                "totalPages": 1,
                                                "last": true,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                  "empty": true,
                                                  "unsorted": true,
                                                  "sorted": false
                                                },
                                                "numberOfElements": 3,
                                                "first": true,
                                                "empty": false
                                              }
                                            }
                                            """
                            )))
    @Operation(summary = "게시글 검색 기능-좋아요 순", description = "로그인 없이 요청 가능한 기능입니다."
    ,tags = "공개APIs"
    )
    Response<Page<PostListResponse>> searchPostByLikes(@Parameter(
            description = "게시글 검색을 위한 키워드 입력.",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "m")}
    ) @PathVariable String keyword,
                                                       @ParameterObject @PageableDefault(page = 0, size = 20)
                                                       Pageable pageable);


    /**
     * 최신포스트 순 - 닉네임 검색
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "키워드에 포함된 닉네임의 유저를 최신 게시글을 올린 유저 순으로 조회합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "키워드 검색 게시글 최신순 조회 성공 예제",
                                    value = """
                                            {
                                                   "resultCode": "SUCCESS",
                                                   "result": {
                                                     "content": [
                                                       {
                                                         "id": 3,
                                                         "nickName": "meh",
                                                         "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-05-00-46-13.jpeg"
                                                       },
                                                       {
                                                         "id": 2,
                                                         "nickName": "monster",
                                                         "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-09-03-14-38-37.jpeg"
                                                       },
                                                       {
                                                         "id": 1,
                                                         "nickName": "MEEP",
                                                         "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-12-21-27-13-2.jpeg"
                                                       },
                                                       {
                                                         "id": 4,
                                                         "nickName": "miracle",
                                                         "profileImageUrl": null
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
                                                     "totalElements": 4,
                                                     "totalPages": 1,
                                                     "last": true,
                                                     "size": 20,
                                                     "number": 0,
                                                     "sort": {
                                                       "empty": true,
                                                       "sorted": false,
                                                       "unsorted": true
                                                     },
                                                     "numberOfElements": 4,
                                                     "first": true,
                                                     "empty": false
                                                   }
                                                 }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인을 안한 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )))
    })
    @Operation(summary = "닉네임 검색 기능-최근 게시글 순", description = "닉네임을 검색하고 최근 게시글을 올린 유저 순으로 가져옵니다")
    Response<Page<SearchNicknamesResponse>> searchNicknamesByRecent(@Parameter(
            description = "닉네임 검색을 위한 키워드 입력.",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "m")}
    ) @PathVariable String keyword, @ParameterObject @PageableDefault(page = 0, size = 20) Pageable pageable);

    /**
     * 팔로우 순- 닉네임 검색
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "키워드에 포함된 닉네임을 팔로우순으로 조회합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "키워드 닉네임 검색 팔로우순으로 조회 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS",
                                               "result": {
                                                 "content": [
                                                   {
                                                     "id": 1,
                                                     "nickName": "MEEP",
                                                     "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-12-21-27-13-2.jpeg"
                                                   },
                                                   {
                                                     "id": 3,
                                                     "nickName": "meh",
                                                     "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-05-00-46-13.jpeg"
                                                   },
                                                   {
                                                     "id": 2,
                                                     "nickName": "monster",
                                                     "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-09-03-14-38-37.jpeg"
                                                   },
                                                   {
                                                     "id": 4,
                                                     "nickName": "miracle",
                                                     "profileImageUrl": null
                                                   }
                                                 ],
                                                 "pageable": {
                                                   "pageNumber": 0,
                                                   "pageSize": 20,
                                                   "sort": {
                                                     "empty": true,
                                                     "unsorted": true,
                                                     "sorted": false
                                                   },
                                                   "offset": 0,
                                                   "paged": true,
                                                   "unpaged": false
                                                 },
                                                 "totalPages": 1,
                                                 "totalElements": 4,
                                                 "last": true,
                                                 "size": 20,
                                                 "number": 0,
                                                 "sort": {
                                                   "empty": true,
                                                   "unsorted": true,
                                                   "sorted": false
                                                 },
                                                 "numberOfElements": 4,
                                                 "first": true,
                                                 "empty": false
                                               }
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인을 안한 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )))
    })
    @Operation(summary = "닉네임 검색 기능-팔로워 순", description = "팔로우 많은 순으로 검색됩니다.")
    Response<Page<SearchNicknamesResponse>> searchNicknamesByFollowers(@Parameter(
            description = "닉네임 검색을 위한 키워드 입력.",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "m"),
                    @ExampleObject(name = "예시2-성공", value = "me"),
                    @ExampleObject(name = "예시3-성공", value = "meep")}
    ) @PathVariable String keyword,@ParameterObject @PageableDefault(page = 0, size = 20)
                                                                       Pageable pageable);

}
