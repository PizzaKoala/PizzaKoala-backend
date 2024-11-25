package com.PizzaKoala.Pizza.global.util;

import com.PizzaKoala.Pizza.global.repository.RefreshRepository;
import com.PizzaKoala.Pizza.member.entity.RefreshToken;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
// JWTUtil- 최신방식 0.12.13 (보통사용하는 JWTUtil-0.11.5와 많이 다르다고 함)
@Component
public class JWTTokenUtils {
    private final SecretKey secretKey;
    private final RefreshRepository refreshRepository;

    public JWTTokenUtils(@Value("${jwt.secret-key}")String secret, RefreshRepository refreshRepository){
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        this.refreshRepository = refreshRepository;
    } //HS256-대칭키양방향 암호화

//    @Value("${jwt.token.expired-time-ms}")
//    private Long expiredTimeMs;

    public String getEmail(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);

    }
    public String getUsername(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("username", String.class);

    }
    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public boolean isExpired(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new java.util.Date());

    }

    public Cookie createCookie(String key, String jwtValue) {
        Cookie cookie = new Cookie(key, jwtValue);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true); // prevent from javascript attack
//        cookie.setSecure(true); <--https 일 경우
        cookie.setPath("/"); //@.@ ㅇㅣ게 맞남..?
        return cookie;
    }

    public String getCategory(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category", String.class);
    }
    public String generatedToken(String category,String email, String role, long expiredTimeMs) {

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



