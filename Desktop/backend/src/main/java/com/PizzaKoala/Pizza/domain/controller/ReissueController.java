package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/")
@RestController
public class ReissueController {
    private final JWTService jwtService;


    public ReissueController(JWTService jwtService) {

        this.jwtService= jwtService;

    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        return jwtService.reissueToken(request.getCookies(), response);
    }
}
