package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.response.AlarmResponse;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "알림APIs")
public interface AlarmControllerDoc {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SSE 알림 연결.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "SSE 연결 성공 예제",
                                    value = """
                                            
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
                            )))
    })
    @Operation(summary = "알림구독",description = "알림을 위한 SSE 구독")
    SseEmitter subscribe(Authentication authentication);
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내알림 리스트 조회.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "내 알림 리스트 조회 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": {
                                                "content": [
                                                  {
                                                    "id": 3,
                                                    "alarmType": "NEW_FOLLOWER",
                                                    "args": {
                                                      "fromMemberId": 2,
                                                      "profilePic": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-09-03-14-38-37.jpeg",
                                                      "nickname": "monster"
                                                    },
                                                    "text": "new follower!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 5,
                                                    "alarmType": "NEW_FOLLOWER",
                                                    "args": {
                                                      "fromMemberId": 3,
                                                      "profilePic": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-05-00-46-13.jpeg",
                                                      "nickname": "meh"
                                                    },
                                                    "text": "new follower!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 7,
                                                    "alarmType": "NEW_FOLLOWER",
                                                    "args": {
                                                      "fromMemberId": 4,
                                                      "nickname": "miracle"
                                                    },
                                                    "text": "new follower!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 9,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 2,
                                                      "profilePic": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-09-03-14-38-37.jpeg",
                                                      "nickname": "monster",
                                                      "postId": 1
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 10,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 3,
                                                      "profilePic": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-05-00-46-13.jpeg",
                                                      "nickname": "meh",
                                                      "postId": 1
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 11,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 4,
                                                      "nickname": "miracle",
                                                      "postId": 1
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 12,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 2,
                                                      "profilePic": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-09-03-14-38-37.jpeg",
                                                      "nickname": "monster",
                                                      "postId": 2
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 13,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 3,
                                                      "profilePic": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-05-00-46-13.jpeg",
                                                      "nickname": "meh",
                                                      "postId": 2
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 14,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 4,
                                                      "nickname": "miracle",
                                                      "postId": 3
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 19,
                                                    "alarmType": "NEW_COMMENT_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 2,
                                                      "profilePic": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-09-03-14-38-37.jpeg",
                                                      "nickname": "monster"
                                                    },
                                                    "text": "new post!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 20,
                                                    "alarmType": "NEW_COMMENT_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 3,
                                                      "profilePic": "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-05-00-46-13.jpeg",
                                                      "nickname": "meh"
                                                    },
                                                    "text": "new post!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
                                                  },
                                                  {
                                                    "id": 21,
                                                    "alarmType": "NEW_COMMENT_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 4,
                                                      "nickname": "miracle"
                                                    },
                                                    "text": "new post!",
                                                    "createdAt": "2025-01-27 22:01:12",
                                                    "modifiedAt": "2025-01-27 22:01:12"
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
                                                "totalElements": 12,
                                                "totalPages": 1,
                                                "last": true,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                  "empty": true,
                                                  "sorted": false,
                                                  "unsorted": true
                                                },
                                                "numberOfElements": 12,
                                                "first": true,
                                                "empty": false
                                              }
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
                            )))
    })
    @Operation(summary = "알람 리스트 조회",description = "내 알람 리스트 조회")
    Response<Page<AlarmResponse>> alarmLists(Authentication authentication,@ParameterObject @PageableDefault(
            page = 0,
            size = 20
    ) Pageable pageable);

    /**
     *알림 단건 삭제 기능
     */

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "단건 알림 성공 예제.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "단건 알림 삭제하기.",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "내 알림ID가 아니거나 로그인이 되어있지 않을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples ={ @ExampleObject(
                                    name = "내 알림이 아닙니다.",
                                    value = """
                                            {
                                               "resultCode": "INVALID_PERMISSION",
                                               "message": "Permission is invalid., You are not accessible for this action"
                                             }
                                            """
                            ), @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            )
                            })),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 알림 삭제 요청일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 알림입니다.",
                                    value = """
                                            {
                                              "resultCode": "ALARM_NOT_FOUND",
                                              "message": "The alarm does not exists., The alarm not found"
                                            }
                                            """
                            )))
    })
    @Operation(summary = "알람 단건 삭제",description = "내 알람 단건 삭제하기")
    Response<Void> deleteAlarm(@Parameter(
            description = "해당 게시글 아이디 입력. *성공예제를 먼저 실행해 주세요.*",
            examples = {
                    @ExampleObject(name = "예시1-성공(3,5,7,9~14)", value = "3"),
                    @ExampleObject(name = "예시2-내 알림 ID가 아닐 경우", value = "1"),
                    @ExampleObject(name = "예시3-존재하지 않는 알림일 경우", value = "0"),
            }
    ) @PathVariable Long alarmId, Authentication authentication) ;

    /**
     *알림 다건 (모두) 삭제 기능
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "알람 다건 삭제 성공.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "내 알람 모두 삭제하기 기능",
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
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없는 경우 발생하는 에러",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "엑세스 토큰을 가지고 있지만 이미 계정은 삭제한 경우",
                                    value = """
                                            {
                                              "resultCode": "MEMBER_NOT_FOUND",
                                              "message": "Meep@kakao.com not found"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "500", description = "트랜젝션이나 데이터베이스 연결 오류일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples = @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "ALARM_DELETE_FAILED",
                                              "message": "Failed to delete alarms."
                                            }
                                            """
                            )))
    })
    @Operation(summary = "알람 다건 삭제",description = "내 알람 모두 삭제하기")
    Response<Void> deleteAllAlarms(Authentication authentication);
}
