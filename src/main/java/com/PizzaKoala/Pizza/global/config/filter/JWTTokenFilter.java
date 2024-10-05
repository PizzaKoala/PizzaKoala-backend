package com.PizzaKoala.Pizza.global.config.filter;

import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Util.JWTTokenUtils;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.model.CustomUserDetailsDTO;
import com.PizzaKoala.Pizza.domain.model.MemberRole;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JWTTokenFilter extends OncePerRequestFilter {
    /**
     * 소셜로그인에는 쿠키에 넣었는데 괜찮은강?
     */
    private final JWTTokenUtils jwtTokenUtils;
    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/*/alarm/subscribe");


    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response,
                                    @Nullable FilterChain filterChain) throws ServletException, IOException {


        if (request == null || response == null || filterChain == null) {
            log.error("Request, Response, or FilterChain is null");
            return;
        }
        String accessToken = null;
        String requestUri = request.getRequestURI();
        if(requestUri.startsWith("/swagger-ui")|| requestUri.startsWith("/v3/spi-docs")||requestUri.startsWith("/h2-console/")){
            filterChain.doFilter(request, response);
            return;
        }

//        if (requestUri.matches("^\\/oauth2(?:\\/.*)?$")) {
        if (requestUri.matches("^/oauth2(?:/.*)?$")) {
            filterChain.doFilter(request, response);
            return; //재로그인 무한로프 방지.
        }

        String header = request.getHeader("Authorization");

        //header가 아니라 request param안에 있을 경우
        if(TOKEN_IN_PARAM_URLS.contains(requestUri)){
            log.info("Request with {} check the query param", request.getRequestURI());
            accessToken= request.getQueryString().split("=")[1].trim();
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
//    }        if (request == null || response == null || filterChain == null) {
//            log.error("Request, Response, or FilterChain is null");
//            return;
//        }
//
//        //header안에 토큰이 있어서 헤더를 꺼내야 한다.
//        String jwt = request.getHeader("Authorization");
//
//        //만약 헤더가 null일떄
//        if (jwt == null || !jwt.startsWith("Bearer ")) {
//            log.error("Error occurs while getting header. header is null or invalid");
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = jwt.split(" ")[1];
//
//        if (jwtTokenUtils.isExpired(token)) {
//            log.error("Error: JWT token is expired.");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        //get email from token
//        String email = jwtTokenUtils.getUsername(token);
//        MemberRole role = jwtTokenUtils.getRole(token);
//        Member member = Member.builder()
//                .email(email)
//                .role(role)
//                .build();
//
//        CustomUserDetailsDTO customUserDetailsDTO = new CustomUserDetailsDTO(member);
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                customUserDetailsDTO, null, customUserDetailsDTO.getAuthorities()
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        filterChain.doFilter(request, response);
//    }

// latest Yumi
//                CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
//
//                Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//
//                filterChain.doFilter(request, response);


    //old version- from fastcampus  TODO: 여기서 이메일 체크 했던건 어디서 하지..?!
//                // check the email is valid
//                // Example using MemberRepository to check if member exists
//                Optional<Member> member = memberRepository.findByEmail(email);
//
//                if (member.isEmpty()) {
//                    log.error("Error: Member with email {} does not exist", email);
//                    filterChain.doFilter(request, response);
//                    return;
//                }
//
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        member.get().getEmail(), null, member.get().getAuthorities()
//                );
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        log.debug("Request path {}", path);

        return path.startsWith("/api/") && path.endsWith("login"); //로그인 중 말고는 항상 실행되게.
    }

    }

