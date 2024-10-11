package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1/")
@RestController
public class ReissueController {
    private final JWTService jwtService;


    public ReissueController(JWTService jwtService) {

        this.jwtService= jwtService;

    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        log.info("실행되낭");
        // 요청에서 쿠키 확인
        if (request.getCookies() == null) {
            log.error("쿠키가 없습니다. 요청을 처리할 수 없습니다.");  // 쿠키가 없을 때 에러 로그
            return ResponseEntity.badRequest().body("쿠키가 없습니다.");
        }

        return jwtService.reissueToken(request.getCookies(), response);
    }
}
