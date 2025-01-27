package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import com.PizzaKoala.Pizza.domain.controller.request.PostCommentCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "달력 컨트롤러", description = "게시글 올린 날들이 달력에 기록되는 기능들")
public interface CalenderControllerDoc {
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "년 단위로 기록을 가져옵니다.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "년 단위 성공 예제",
                                    value = """
                                            {
                                              "resultCode": "SUCCESS",
                                              "result": [
                                                [
                                                  2025,1,25
                                                ]
                                              ]
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인을 안했을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples =
                            @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 계정입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCommentCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 계정입니다",
                                    value = """
                                             {
                                               "resultCode": "MEMBER_NOT_FOUND",
                                               "message": "The user does not exist."
                                             }
                                            """
                            ))),
    })
    @Operation(summary = "년 단위 검색", description = "일년 차트로 보여줍니다.")
    Response<List<LocalDate>> getYearlyCalendar(@PathVariable int year, @PathVariable Long memberId);

    //monthly calendar
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월 단위로 기록을 가져옵니다.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "년 단위 성공 예제",
                                    value = """
                                            [
                                              "2025-01-25"
                                            ]
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인을 안했을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples =
                            @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            ))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 계정입니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostCommentCreateRequest.class),
                            examples = @ExampleObject(
                                    name = "존재하지 않는 계정입니다",
                                    value = """
                                             {
                                               "resultCode": "MEMBER_NOT_FOUND",
                                               "message": "The user does not exist."
                                             }
                                            """
                            ))),
    })
    @Operation(summary = "월 단위로 검색", description = "달 단위로 보여줍니다. ")
    List<LocalDate> getMonthlyCalendar(@PathVariable int year, @PathVariable int month, @PathVariable Long memberId);
}
