package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.response.AlarmResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "알림 기능 컨트롤러", description = "실시간 SSE 알림, 내 알림 리스트")
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
                                                    "id": 14,
                                                    "alarmType": "NEW_COMMENT_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 4,
                                                      "targetId": 1
                                                    },
                                                    "text": "new post!",
                                                    "createdAt": "2025-01-26 16:24:21",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:21"
                                                  },
                                                  {
                                                    "id": 13,
                                                    "alarmType": "NEW_COMMENT_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 3,
                                                      "targetId": 1
                                                    },
                                                    "text": "new post!",
                                                    "createdAt": "2025-01-26 16:24:21",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:21"
                                                  },
                                                  {
                                                    "id": 12,
                                                    "alarmType": "NEW_COMMENT_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 2,
                                                      "targetId": 1
                                                    },
                                                    "text": "new post!",
                                                    "createdAt": "2025-01-26 16:24:21",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:21"
                                                  },
                                                  {
                                                    "id": 10,
                                                    "alarmType": "NEW_COMMENT_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 1,
                                                      "targetId": 1
                                                    },
                                                    "text": "new post!",
                                                    "createdAt": "2025-01-26 16:24:21",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:21"
                                                  },
                                                  {
                                                    "id": 6,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 4,
                                                      "targetId": 3
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-26 16:24:21",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:21"
                                                  },
                                                  {
                                                    "id": 5,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 3,
                                                      "targetId": 2
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-26 16:24:21",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:21"
                                                  },
                                                  {
                                                    "id": 4,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 2,
                                                      "targetId": 2
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-26 16:24:21",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:21"
                                                  },
                                                  {
                                                    "id": 3,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 4,
                                                      "targetId": 1
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-26 16:24:21",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:21"
                                                  },
                                                  {
                                                    "id": 2,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 3,
                                                      "targetId": 1
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-26 16:24:21",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:21"
                                                  },
                                                  {
                                                    "id": 1,
                                                    "alarmType": "NEW_Like_ON_POST",
                                                    "args": {
                                                      "fromMemberId": 2,
                                                      "targetId": 1
                                                    },
                                                    "text": "new like!",
                                                    "createdAt": "2025-01-26 16:24:20",
                                                    "deletedAt": null,
                                                    "modifiedAt": "2025-01-26 16:24:20"
                                                  }
                                                ],
                                                "pageable": {
                                                  "pageNumber": 0,
                                                  "pageSize": 20,
                                                  "sort": {
                                                    "empty": false,
                                                    "unsorted": false,
                                                    "sorted": true
                                                  },
                                                  "offset": 0,
                                                  "paged": true,
                                                  "unpaged": false
                                                },
                                                "totalElements": 10,
                                                "totalPages": 1,
                                                "last": true,
                                                "size": 20,
                                                "number": 0,
                                                "sort": {
                                                  "empty": false,
                                                  "unsorted": false,
                                                  "sorted": true
                                                },
                                                "numberOfElements": 10,
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
}
