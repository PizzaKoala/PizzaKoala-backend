package com.PizzaKoala.Pizza.global.config.filter;

import com.PizzaKoala.Pizza.global.util.JWTTokenUtils;
import com.PizzaKoala.Pizza.member.entity.Member;
import com.PizzaKoala.Pizza.domain.dto.CustomUserDetailsDTO;
import com.PizzaKoala.Pizza.member.entity.enums.MemberRole;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class JWTTokenFilter extends OncePerRequestFilter {
    /**
     * 소셜로그인에는 쿠키에 넣었는데 괜찮은강?
     */
    private final JWTTokenUtils jwtTokenUtils;
//    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/*/alarm/subscribe");


    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
                                    @Nullable FilterChain filterChain) throws ServletException, IOException {


        if (request == null || response == null || filterChain == null) {
            log.error("Request, Response, or FilterChain is null");
            return;
        }
        String accessToken = null;
        String requestUri = request.getRequestURI();
        if(requestUri.startsWith("/swagger-ui")|| requestUri.startsWith("/v3/spi-docs")
                ||requestUri.startsWith("/h2-console/")||requestUri.startsWith("/favicon.ico")){
            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.startsWith("/login") || requestUri.startsWith("/oauth2/")||requestUri.startsWith("/api/v1/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }


        if (requestUri.matches("^/login(?:/.*)?$")) {

            filterChain.doFilter(request, response);
            return;
        }
        if (requestUri.matches("^/oauth2(?:/.*)?$")) {

            filterChain.doFilter(request, response);
            return;//재로그인 무한로프 방지.
        }


        String header = request.getHeader("Authorization");

        //알람이 요청될때는 엑세스 토큰을 헤더가 아닌 쿠키에 담아서 보내준다.

        if(requestUri.startsWith("/api/*/alarm/subscribe")){
            log.info("Request with {} check the cookie _ SSE_ALARM_SUBSCRIBE", request.getRequestURI());
            for (Cookie cookie : request.getCookies()) {
                if ("Authorization".equals(cookie.getName())) {
                    accessToken= cookie.getValue().substring(7);
                }
            }
            if (accessToken == null) {
                log.error("Error occurs while getting access token from cookie, cookie is null or invalid {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }
        } else if (header != null && header.startsWith("Bearer ")) {
            //header에 access token 이 있을 경우
            accessToken = header.substring(7);
        }else {
            //토큰이 없다면 다음 필터로 넘김
            log.error("Error occurs while getting header, header is null or invalid {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

//        if (requestUri.matches("^\\/login(?:\\/.*)?$")) {
//
//            filterChain.doFilter(request, response);
//            return;
//        }



        // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음 - dofilter 안함!!!
        try {
            jwtTokenUtils.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired.");

            //request status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //토큰이 access인지 확인(발급시 페이로드에 명시/ access토큰이 아니면 다음 필터로 넘기면 안됨 (dofilter 안함!!!)
        String category = jwtTokenUtils.getCategory(accessToken);

        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        //get email & role from token
        String email = jwtTokenUtils.getUsername(accessToken);
        String role = jwtTokenUtils.getRole(accessToken);

        Member member = Member.builder()
                .email(email)
                .role(MemberRole.valueOf(role))
                .build();
        CustomUserDetailsDTO customUserDetailsDTO = new CustomUserDetailsDTO(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customUserDetailsDTO, null, customUserDetailsDTO.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        log.debug("Request path {}", path);

        return path.startsWith("/api/") && path.endsWith("login"); //로그인 중 말고는 항상 실행되게.
    }

    }

