package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import com.PizzaKoala.Pizza.domain.controller.request.PostCommentCreateRequest;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "캘린더APIs")
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
                                                "2025-01-27"
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
            @ApiResponse(responseCode = "400", description = "유효하지않는 년도일 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples =
                            @ExampleObject(
                                    name = "유효하지 않는 년도입니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_YEAR",
                                              "message": "Invalid year provided"
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
    Response<List<LocalDate>> getYearlyCalendar(@Parameter(
            description = "유저 아이디를 입력해주세요.",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "2025"),
                    @ExampleObject(name = "예시2-실패", value = "2000"),
                    @ExampleObject(name = "예시3-실패", value = "2050")}
    ) @PathVariable int year, @PathVariable Long memberId);

    //monthly calendar
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "월 단위로 기록을 가져옵니다.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "년 단위 성공 예제",
                                    value = """
                                            [
                                              "2025-01-27",
                                              "2025-01-29",
                                              "2025-01-30"
                                            ]
                                            """
                            ))),
            @ApiResponse(responseCode = "401", description = "로그인을 안했을 경우",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCode.class),
                            examples ={
                            @ExampleObject(
                                    name = "로그인해야 사용할수있는 기능입니다",
                                    value = """
                                            {
                                              "resultCode": "INVALID_TOKEN",
                                              "message": "Full authentication is required to access this resource"
                                            }
                                            """
                            ),@ExampleObject(
                                    name = "유효하지 않는 년도입니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_YEAR",
                                              "message": "Invalid year provided"
                                            }
                                            """
                            ),@ExampleObject(
                                    name = "유효하지 않는 월입니다.",
                                    value = """
                                            {
                                              "resultCode": "INVALID_MONTH",
                                              "message": "Invalid month provided"
                                            }
                                            """
                            )
                    })),
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
    List<LocalDate> getMonthlyCalendar(@Parameter(
            description = "검색할 년도를 입력해주세요.",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "2025"),
                    @ExampleObject(name = "예시2-실패", value = "2000"),
                    @ExampleObject(name = "예시3-실패", value = "2050")}
    ) @PathVariable int year, @Parameter(
            description = "검색한 월을 입력해주세요.",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "2"),
                    @ExampleObject(name = "예시2-실패", value = "0"),
                    @ExampleObject(name = "예시3-성공", value = "13")}
    ) @PathVariable int month,@Parameter(
            description = "검색한 월을 입력해주세요.",
            examples = {
                    @ExampleObject(name = "예시1-성공", value = "1"),
                    @ExampleObject(name = "예시2-성공", value = "2"),
                    @ExampleObject(name = "예시3-실패", value = "0")}
    ) @PathVariable Long memberId);
}
