package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Util.JWTTokenUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final JWTTokenUtils jwtTokenUtils;
    public void handleSuccessfulAuthentication(String email, String role,HttpServletResponse response) {
        String access = jwtTokenUtils.generatedToken("access",email, role, 600000L);
        String refresh = jwtTokenUtils.generatedToken("refresh",email, role, 86400000L);
        response.setHeader("access", access);
        response.addCookie(jwtTokenUtils.createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

        //refresh 토큰 db에 저장
        jwtTokenUtils.addRefreshEntity(email,refresh,86400000L);
    }
}
