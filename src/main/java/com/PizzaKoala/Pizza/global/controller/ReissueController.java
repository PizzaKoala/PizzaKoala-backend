package com.PizzaKoala.Pizza.global.controller;

import com.PizzaKoala.Pizza.global.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1/")
@RestController
public class ReissueController {
    /**
     * refreshtoken entity는 맴버당 하나씩만 저장되기 때문에 맴버 폴더에 속해있고
     * reissuecontroller는 인증 authorization에 관한거니 global에 속해있게 결정했다.
     */
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
