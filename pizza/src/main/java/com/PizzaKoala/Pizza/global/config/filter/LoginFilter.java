//package com.PizzaKoala.Pizza.global.config.filter;
//
//import com.PizzaKoala.Pizza.domain.Util.JWTTokenUtils;
//import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
//import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
//import com.PizzaKoala.Pizza.domain.model.CustomUserDetailsDTO;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationServiceException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Iterator;
//@Slf4j
//public class LoginFilter extends UsernamePasswordAuthenticationFilter {
//    private final AuthenticationManager authenticationManager;
//    private final JWTTokenUtils jwtTokenUtils;
//    public LoginFilter(AuthenticationManager authenticationManager,JWTTokenUtils jwtTokenUtils) {
//
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenUtils = jwtTokenUtils;
//    }
//// throws AuthenticationException
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
//        log.debug("attemptAuthentication started...");
//        if (obtainUsername(request) != null) {
//
//            throw new PizzaAppException(ErrorCode.DUPLICATED_EMAIL_ADDRESS, "Meep");
//        }
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//        if (username==null) {
//            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
//        }
//        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password,null);
//        setDetails(request,authToken);
//
//        return authenticationManager.authenticate(authToken); //AuthenticationManager가 검증을 담당한다.
//        //성공하면 successfulAuthentication가 실행되고 실패하면 unsuccessfulAuthentication이 실행된다.
//        //만든 필터는 security config에 등록한다
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//
//        CustomUserDetailsDTO customUserDetailsDTO = (CustomUserDetailsDTO) authentication.getPrincipal();
//
//       String email= customUserDetailsDTO.getUsername();
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
//        GrantedAuthority auth = iterator.next();
//
//        String role = auth.getAuthority();
//
//
//        //토큰 생성
////        String access = jwtTokenUtils.generatedToken("access", email, role, 600000L);
//        String refresh = jwtTokenUtils.generatedToken(email, role, 86400000L);
////        String refresh = jwtTokenUtils.generatedToken("refresh", email, role, 86400000L);
//        System.out.println("Generated Token: " + refresh);
//        response.addHeader("Authorization","Bearer "+refresh);
//
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        log.debug("Generated Refresh Token: {}", refresh);
//
////        //응답 설정
////        response.setHeader("access", access);
////        response.addCookie(createCookie("refresh", refresh));
////        response.setStatus(HttpStatus.OK.value());
////        System.out.println("email = " + email);  //TODO 왜 안되지 :(
////        log.error(" email= "+email);
//    }
//
//    private Cookie createCookie(String key, String value) {
//        Cookie cookie = new Cookie(key, value);
//        cookie.setMaxAge(24 * 60 * 60);
//        cookie.setHttpOnly(true); //클라이언트 단에서 자바스크립트로 해당 쿠키에 접근하지 못하도록 필수적으로 막아둔다.
//        return cookie;
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
////        log.error("Error: Member with email {} does not exist","@.@");
//        log.error("Error: unsuccessful Authentication.");
//        response.setStatus(401);
//
//        super.unsuccessfulAuthentication(request, response, failed);
//
//    }
//}
//
