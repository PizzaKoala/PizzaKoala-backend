package com.PizzaKoala.Pizza.domain.controller.swagInterface;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "리프레쉬 토큰 발급 컨트롤러", description = "토큰 재발급 요청을 합니다.")
public interface ReissueControllerDoc {
    @Operation(summary = "토큰 재발급", description = "")
    ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response);
}
