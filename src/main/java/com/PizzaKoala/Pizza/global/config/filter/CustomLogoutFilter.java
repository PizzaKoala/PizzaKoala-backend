package com.PizzaKoala.Pizza.global.config.filter;

import com.PizzaKoala.Pizza.global.repository.RefreshRepository;
import com.PizzaKoala.Pizza.global.util.JWTTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {
    private final JWTTokenUtils jwtTokenUtils;
    private final RefreshRepository refreshRepository;

    public CustomLogoutFilter(JWTTokenUtils jwtTokenUtils, RefreshRepository refreshRepository) {
        this.jwtTokenUtils = jwtTokenUtils;
        this.refreshRepository = refreshRepository;

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {




        //path and method verify
        String requestUri = request.getRequestURI();
        String requestMethod = request.getMethod();
        log.debug("Incoming request URI: {}, Method: {}", requestUri, requestMethod);

        if (!requestUri.matches("^/api/v1/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }


        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        System.out.println("cookie = " + Arrays.toString(request.getCookies()));
        log.trace("cookie = " + Arrays.toString(request.getCookies()));

        if (cookies == null) {
            log.error("empty cookie");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }



        //refresh null check
        if (refresh == null) {
            log.error("refresh null");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //expired check
        try {
            jwtTokenUtils.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            log.error("already logged out");
            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); //TODO: 이미 로그아웃 되어있다.
            return;
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtTokenUtils.getCategory(refresh);
        if (!category.equals("refresh")) {
            log.error("category dosent contain refresh token");
            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            log.error("Already logged out");
            //response status code
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //로그아웃 진행- token id와 같은것만 지우는게 아니라 이메일 같은걸 지워서 같은 이메일 중복 막음
        //Refresh 토큰 DB에서 제거
//        refreshRepository.deleteByRefresh(refresh);
        refreshRepository.clearRefreshTokenByRefresh(refresh);

        //Refresh 토큰 Cookie 값 0
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
