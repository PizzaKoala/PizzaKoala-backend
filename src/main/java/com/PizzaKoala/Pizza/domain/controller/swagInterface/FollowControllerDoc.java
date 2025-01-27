package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.response.FollowListResponse;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "팔로우 컨트롤러", description = "팔로우 기능들")
public interface FollowControllerDoc {
    /**
     * 유저 팔로우 하기
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저를 팔로우합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "맴버 팔로우 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS"
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "자신의 계정을 팔로우하려 한 경우, 로그인을 안한 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            ), @ExampleObject(
                                    name = "자신의 계정을 팔로우하려 한 경우",
                                    value = """
                                            {
                                               "resultCode": "INVALID_PERMISSION",
                                               "message": "Permission is invalid., You cannot follow your own account."
                                             }
                                            """
                            )})),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 계정일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 계정입니다.",
                                    value = """
                                            {
                                              "resultCode": "FOLLOWING_ID_NOT_FOUND",
                                              "message": "The user you are trying to follow does not exist"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "409", description = "이미 팔로우 하고 있는 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "이미 팔로우하고 있는 계정입니다.",
                                    value = """
                                              {
                                                "resultCode": "ALREADY_FOLLOWED",
                                                "message": "User has already followed the account., You have are already followed 222"
                                              }
                                            """
                            )))
    })
    @Operation(summary = "맴버 팔로우 기능", description = "다른 유저를 팔로우하는 기능")
    Response<Void> followAMember(Authentication authentication, @Parameter(
            description = "해당 게시글 아이디 입력. *성공예제를 먼저 실행해 주세요.*",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "4"),
                    @ExampleObject(name = "예시2-자신을 팔로우할 경우", value = "1"),
                    @ExampleObject(name = "예시3-이미 팔로우한 게시글일 경우", value = "2"),
                    @ExampleObject(name = "예시4-존재하지않는 계정일 경우", value = "10004")
            }
    ) @PathVariable Long following);

    /**
     * 언팔로우 하기
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "맴버를 언팔로우합니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "언팔로우 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS"
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인X,자신계정,팔로우 하지않은 계정일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            ), @ExampleObject(
                                    name = "자신의 계정을 언팔로우하려 한 경우",
                                    value = """
                                            {
                                              "resultCode": "INVALID_PERMISSION",
                                              "message": "Permission is invalid., You cannot unfollow yourself"
                                            }
                                            """
                            ), @ExampleObject(
                                    name = "팔로우하고 있지 않는 경우",
                                    value = """
                                            {
                                                "resultCode": "FOLLOW_NOT_FOUND",
                                                "message": "This follow relationship does not exist., You are not following this user."
                                              }
                                            """
                            )})),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 계정일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 계정입니다.",
                                    value = """
                                            {
                                              "resultCode": "FOLLOWING_ID_NOT_FOUND",
                                              "message": "The user you are trying to follow does not exist"
                                            }
                                            """
                            )))
    })
    @Operation(summary = "맴버 언팔로우 기능", description = "팔로우 취소하는 기능")
    Response<Void> unfollowAMember(Authentication authentication, @Parameter(
            description = "해당 게시글 아이디 입력. *성공예제를 먼저 실행해 주세요.*",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "3"),
                    @ExampleObject(name = "예시2-자신을 팔로우할 경우", value = "1"),
                    @ExampleObject(name = "예시3-팔로우하지 않은 계정일 경우", value = "4"),
                    @ExampleObject(name = "예시4-존재하지않는 계정일 경우", value = "10004")
            }
    ) @PathVariable Long followingId);

    /**
     * 나의 팔로워의 팔로우 끊기
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로우를 끊습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "팔로우 끊기 성공 예제",
                                    value = """
                                            {
                                               "resultCode": "SUCCESS"
                                             }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "자신을 언팔로우,팔로우하지 않은 유저, 로그인이 되어있지 않을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "자신의 계정을 언팔로우하려 한 경우",
                                    value = """
                                            {
                                              "resultCode": "INVALID_PERMISSION",
                                              "message": "Permission is invalid., You cannot perform this action on your own account."
                                            }
                                            """
                            ), @ExampleObject(
                                    name = "팔로우하고 있지 않는 경우",
                                    value = """
                                            {
                                                "resultCode": "FOLLOW_NOT_FOUND",
                                                "message": "This follow relationship does not exist., You are not following this user."
                                              }
                                            """
                            ), @ExampleObject(
                                    name = "로그인하고 있지 않는 경우",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )})),
            @ApiResponse(responseCode = "404", description = "존재 하지 않는 계정일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 계정입니다.",
                                    value = """
                                            {
                                              "resultCode": "MEMBER_NOT_FOUND",
                                              "message": "This follow relationship does not exist., This user is not following you."
                                            }
                                            """
                            )))
    })
    @Operation(summary = "팔로우 삭제 기능", description = "나를 팔로우하는 사람의 팔로우를 내가 취소하는 기능.(광고나 싫은 사람이 내 계정 팔로우하는거 싫을 경우 사용")
    Response<Void> deleteAFollower(Authentication authentication, @Parameter(
            description = "해당 게시글 아이디 입력. *성공예제를 먼저 실행해 주세요.*",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "2"),
                    @ExampleObject(name = "예시2-자신의 계정을 요청 경우", value = "1"),
                    @ExampleObject(name = "예시3-팔로우하지 않은 계정일 경우", value = "4"),
                    @ExampleObject(name = "예시4-존재하지않는 계정일 경우", value = "10004")
            }
    ) @PathVariable Long followerId);

    /**
     * 나의 팔로워 & 팔로우 리스트
     * {or} -1 for my followers -2 for users I am following
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저의 팔로워/팔로잉 리스트를 가져옵니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = {@ExampleObject(
                                    name = "유저의 팔로워 리스트 성공 예제",
                                    value = """
                                            {
                                                 "resultCode": "SUCCESS",
                                                 "result": {
                                                   "content": [
                                                     {
                                                       "id": 4,
                                                       "nickName": "444",
                                                       "profileImageUrl": null
                                                     },
                                                     {
                                                       "id": 3,
                                                       "nickName": "333",
                                                       "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-05-00-46-13.jpeg"
                                                     },
                                                     {
                                                       "id": 2,
                                                       "nickName": "222",
                                                       "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-09-03-14-38-37.jpeg"
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
                                                   "totalElements": 3,
                                                   "last": true,
                                                   "size": 20,
                                                   "number": 0,
                                                   "sort": {
                                                     "empty": true,
                                                     "sorted": false,
                                                     "unsorted": true
                                                   },
                                                   "numberOfElements": 3,
                                                   "first": true,
                                                   "empty": false
                                                 }
                                               }
                                            """
                            ), @ExampleObject(
                                    name = "유저의 팔로잉 리스트 성공 예제",
                                    value = """
                                               {
                                                                        "resultCode": "SUCCESS",
                                                                        "result": {
                                                                          "content": [
                                                                            {
                                                                              "id": 3,
                                                                              "nickName": "333",
                                                                              "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-05-00-46-13.jpeg"
                                                                            },
                                                                            {
                                                                              "id": 2,
                                                                              "nickName": "222",
                                                                              "profileImageUrl": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-09-03-14-38-37.jpeg"
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
                                                                          "totalElements": 2,
                                                                          "last": true,
                                                                          "size": 20,
                                                                          "number": 0,
                                                                          "sort": {
                                                                            "empty": true,
                                                                            "sorted": false,
                                                                            "unsorted": true
                                                                          },
                                                                          "numberOfElements": 2,
                                                                          "first": true,
                                                                          "empty": false
                                                                        }
                                                                      }
                                            """
                            )})),
            @ApiResponse(responseCode = "401", description = "1,2 이외의 숫자나 문자이 요청되었거나 로그인이 되지 않았을때 ",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = {@ExampleObject(
                                    name = "1,2가 아닌 요청일때.",
                                    value = """
                                            {
                                               "resultCode": "INVALID_PERMISSION",
                                               "message": "Permission is invalid., follower list-1, following list-2"
                                               }
                                            """
                            ), @ExampleObject(
                                    name = "로그인하고 있지 않는 경우",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )}))})
    @Operation(summary = "팔로우, 팔로워 다건조회", description = "1- 내 팔로워 리스트, 2- 내 팔로잉 리스트")
    Response<Page<FollowListResponse>> followList(Authentication authentication, @ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable pageable, @Parameter(
            description = "해당 게시글 아이디 입력. *성공예제를 먼저 실행해 주세요.*",
            examples = {
                    @ExampleObject(name = "예시1-성공_내 팔로워 리스트", value = "1"),
                    @ExampleObject(name = "예시2-성공_내 팔로잉 리스트", value = "2"),
                    @ExampleObject(name = "예시3-존재하지 않는 파라미터 요청일 경우", value = "3")}
    ) @PathVariable int or);

}
