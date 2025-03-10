package com.PizzaKoala.Pizza.domain.Util;

import com.PizzaKoala.Pizza.domain.Repository.RefreshRepository;
import com.PizzaKoala.Pizza.domain.entity.RefreshToken;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
// JWTUtil- 최신방식 0.12.13 (보통사용하는 JWTUtil-0.11.5와 많이 다르다고 함)
@Slf4j
@Component
@PropertySource("classpath:/application-deploy.yaml")
public class JWTTokenUtils {
    private final SecretKey secretKey;
    private final RefreshRepository refreshRepository;

    @PostConstruct
    public void init(){
        log.info("✅ JWT Secret Key: {}", secretKey);  // 서버 실행 시 로드된 값 출력
    }
    public JWTTokenUtils(@Value("${jwt.secret-key}")String secret, RefreshRepository refreshRepository){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshRepository = refreshRepository;
        log.info("JWTTokenUtils 생성자 호출됨. secretKey 초기화 완료.");
    } //HS256-대칭키양방향 암호화




//    @Value("${jwt.token.expired-time-ms}")
//    private Long expiredTimeMs;

    public String getEmail(String token) {
        log.info("getEmail() 호출됨. 토큰: {}", token);
        try {
            String email = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
            log.info("getEmail() 성공. email: {}", email);
            return email;
        } catch (Exception e) {
            log.error("getEmail() 실패. 토큰: {}, 오류: {}", token, e.getMessage());
            return null;
        }
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);

    }
    public String getUsername(String token) {
        log.info("getUsername() 호출됨. 토큰: {}", token);
        try {
            String username = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);
            log.info("getUsername() 성공. username: {}", username);
            return username;
        } catch (Exception e) {
            log.error("getUsername() 실패. 토큰: {}, 오류: {}", token, e.getMessage());
            return null;
        }
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);

    }
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new java.util.Date());

    }

    public Cookie createCookie(String key, String jwtValue) {
        log.info("createCookie() 호출됨. key: {}, jwtValue: {}", key, jwtValue);
        Cookie cookie = new Cookie(key, jwtValue);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true); // prevent from javascript attack
//        cookie.setSecure(true); <--https 일 경우
        cookie.setPath("/"); //@.@ ㅇㅣ게 맞남..?
        log.info("createCookie() 성공. 쿠키 생성 완료.");
        return cookie;
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }
    public String generatedToken(String category,String email, String role, long expiredTimeMs) {
        log.info("jwt token gernerate "+ email);

        return Jwts.builder()
                .claim("category", category)
                .claim("role",role)
                .claim("username",email)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(secretKey)
                .compact();
    }
    public void addRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);
        // 이메일로 RefreshToken을 조회합니다.
        RefreshToken existingToken = refreshRepository.findByEmail(email);

        /**
         * 토큰도 로그인할떄마다 엔티티들이 생셔서 하나로 수정하며 사용해야한다.. 저번에는 잘 작동했는데 :(
         * 이거 고침!! 다음에 블로그에 써보자!
         *
         * 로그아웃할떄 logout 경로 잘못읽어온다 /api/* /logout 이렇게 하면 안됐고 /api/v1/logout 이렇게 하면 됬음
         * 로그아웃할떄 강의에선 attribute를 아예 삭제 했는데 난 그냥 토큰 부분만 null로 해줬다.
         *
         * 로그인 할떄 jwt refresh 토큰(쿠키에 보관)이 db에 저장된다.(보안 떄문)
         * 문제- 로그인를 두번 해보면 db에 토큰이 두개였다(기존 토큰이 안사라짐)
         * 그래서 JWTTokenUtils.java 에 있는 addRefreshEntity method에 이메일로 refresh entity에 이미 존재하는지
         * 확인하는 메서드, 존재하면 refresh 이 비어있으면 새로 db에 저장하도록 해주었다.
         */
        if (existingToken != null) {
            // 토큰이 이미 존재하면 기존 토큰의 값을 업데이트합니다.
            refreshRepository.updateRefreshTokenByRefresh(refresh,date.toString(),email);
        } else {
            // 토큰이 존재하지 않으면 새로운 토큰을 생성합니다.
            RefreshToken refreshToken = RefreshToken.builder()
                    .email(email)
                    .refresh(refresh)
                    .expiration(date.toString())
                    .build();
            refreshRepository.save(refreshToken);
        }
    }

}



