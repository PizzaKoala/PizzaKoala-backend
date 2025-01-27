package com.PizzaKoala.Pizza.domain.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
@Schema(description = "유저 로그인 요청")
public class UserLoginRequest {
    @Schema(description = "사용자 이메일", example = "Meep@kakao.com")
    private String email;
    @Schema(description = "사용자 비밀번호", example = "123")
    private String password;
}
