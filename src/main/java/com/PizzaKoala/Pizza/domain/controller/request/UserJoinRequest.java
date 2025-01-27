package com.PizzaKoala.Pizza.domain.controller.request;

import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@Schema(description = "회원가입 유저 요청")
public class UserJoinRequest {
    @Schema(description = "닉네임을 정해주세요", example = "텔레토비")
    private String nickName;
    @Schema(description = "로그인 할때 사용할 이메일을 입력해주세요", example = "example@kakao.com")
    private String email;
    @Schema(description = "비밀번호를 입력해주세요.", example = "password123")
    private String password;
}
