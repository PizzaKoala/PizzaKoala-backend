package com.PizzaKoala.Pizza.global.config.filter;

import com.PizzaKoala.Pizza.domain.controller.request.UserLoginRequest;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.service.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class NewLoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;

    public NewLoginFilter(AuthenticationManager authenticationManager, AuthenticationService authenticationService) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        setFilterProcessesUrl("/api/v1/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserLoginRequest userLoginRequest = objectMapper.readValue(request.getInputStream(), UserLoginRequest.class);
//            String password = obtainPassword(request);
//            String email = request.getParameter("email");
            String password = userLoginRequest.getPassword();
            String email = userLoginRequest.getEmail();

            if (email == null || password == null) {
                throw new AuthenticationServiceException("Email or Password not provided");
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to parse request body", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = authResult.getName();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

//        String access = jwtTokenUtils.generatedToken("access",email, role, 600000L);
//        String refresh = jwtTokenUtils.generatedToken("refresh",email, role, 86400000L);
//        response.setHeader("access", access);
//        response.addCookie(jwtTokenUtils.createCookie("refresh", refresh));
//        response.setStatus(HttpStatus.OK.value());
//
//        //refresh 토큰 db에 저장
//        jwtTokenUtils.addRefreshEntity(email,refresh,86400000L);

        authenticationService.handleSuccessfulAuthentication(email, role, response);

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("Authentication failed: " + failed.getMessage());

        //서류 메세지 설정
        String errorMessage="";
        ErrorCode errorCode = ErrorCode.INVALID_PERMISSION;
        if (failed instanceof BadCredentialsException) {
            errorMessage = "Invalid email or password.";
        }
        // JSON 응답 구성
        String jsonResponse = String.format(
                "{\"errorCode\": \"%s\", \"errorMessage\": \"%s\"}",
                errorCode, errorMessage
        );

        response.getWriter().write(jsonResponse);
    }


}
