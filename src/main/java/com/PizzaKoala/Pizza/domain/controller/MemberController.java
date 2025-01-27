package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.request.UserJoinRequest;
import com.PizzaKoala.Pizza.domain.controller.request.UserLoginRequest;
import com.PizzaKoala.Pizza.domain.controller.response.ErrorResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.response.UserJoinResponse;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "회원 컨트롤러", description = "회원가입, 로그인 API- 로그인/가입 후 response header에서 JWT 토큰을(Bearer) 복사해서 인증(Authorize) 해주세요. 이 글 위에 오른쪽에 [Authorize] 버튼누르고 붙여넣기하면 됩니다.")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입이 완료되었습니다.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserJoinResponse.class),
                            examples = @ExampleObject(
                                    name = "회원가입 성공 예제",
                                    value = "{\n" +
                                            "  \"resultCode\": \"SUCCESS\",\n" +
                                            "  \"result\": {\n" +
                                            "    \"id\": 13,\n" +
                                            "    \"email\": \"examp2le@kakao.com\",\n" +
                                            "    \"role\": \"USER\",\n" +
                                            "    \"profileImageUrl\": \"https://s3.ap-northeast-2.amazonaws.com/pizzakoala/50daeb35-17b0-4bae-a3fe-2d0a34132bfe.jpeg\"\n" +
                                            "  }\n" +
                                            "}"
                            ))),
            @ApiResponse(responseCode = "409", description = "중복 이메일, 닉네임일때 에러 반환",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "인증 실패 예제 - Duplicated Email",
                                            value = "{\n" +
                                                    "  \"resultCode\": \"DUPLICATED_EMAIL_ADDRESS\",\n" +
                                                    "  \"result\": null\n" +
                                                    "}"
                                    ),
                                    @ExampleObject(
                                            name = "인증 실패 예제 - Duplicated Nickname",
                                            value = "{\n" +
                                                    "  \"resultCode\": \"DUPLICATED_NICKNAME\",\n" +
                                                    "  \"result\": null\n" +
                                                    "}"
                                    )
                            }))})
    @Operation(summary = "회원가입 with 프로필 사진", description = "프로필 사진과 함께 회원가입 해주세요. 사진 올리지 않으면 실행되지않아요!")
    @PostMapping(value = "/join", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<UserJoinResponse> join(@RequestPart("file") MultipartFile file,
                                           @RequestPart("request") UserJoinRequest request, HttpServletResponse response) throws IOException {

        UserDTO joinUserDTO = memberService.join(file, request.getNickName(), request.getEmail(), request.getPassword(), response);
        return Response.success(UserJoinResponse.fromJoinUserDTO(joinUserDTO));

    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인이 완료되었습니다.(response header에 토큰 반환)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "resultCode:401, 존재하지 않는 이메일이나 비밀번호 입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "인증 실패 예제",
                                    value = "{\n" +
                                            "  \"errorCode\": \"INVALID_PERMISSION\",\n" +
                                            "  \"errorMessage\": \"Invalid email or password.\"\n" +
                                            "}"
                            )
                    ))
    })
    @Operation(summary = "로그인 API with 이메일, 비밀번호", description = "(테스트용:Meep@kakao.com, 비밀번호-123) 로그인시 jwt토큰이 발행됩니다."
            , requestBody = @RequestBody(content = @Content(
            examples = {
                    @ExampleObject(name = "예시1-성공", value = """
                                {
                                    "email" : "Meep@kakao.com",
                                    "password" : "123"
                                }
                            """),
                    @ExampleObject(name = "예시2-틀린 비밀번호", value = """
                                {
                                    "email" : "Meep@kakao.com",
                                    "password" : "df"
                                }
                            """),
                    @ExampleObject(name = "예시3-존재하지않는 계정", value = """
                                {
                                    "email" : "Meepeeeee@kakao.com",
                                    "password" : "123"
                                }
                            """),
            })))
    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Response<Void> login(@org.springframework.web.bind.annotation.RequestBody UserLoginRequest request) {
        memberService.login(request.getEmail(), request.getPassword());
        return Response.success(null);
    }


}
