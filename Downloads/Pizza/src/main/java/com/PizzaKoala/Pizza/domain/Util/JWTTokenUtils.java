package com.PizzaKoala.Pizza.domain.Util;

import com.PizzaKoala.Pizza.domain.Repository.RefreshRepository;
import com.PizzaKoala.Pizza.domain.entity.RefreshToken;
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
//TODO: ?
//    private Claims extractClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(secretKey)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }

    public Cookie createCookie(String key, String jwtValue) {
        Cookie cookie = new Cookie(key, jwtValue);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true); // prevent from javascript attack
//        cookie.setSecure(true); <--https 일 경우
//        cookie.setPath("/");
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
            RefreshToken refreshToken= RefreshToken.builder()
                    .email(email)
                    .refresh(refresh)
                    .expiration(date.toString())
                    .build();
        refreshRepository.save(refreshToken);
    }

}


//@Component
//public class JWTTokenUtils {
//    @Value("${jwt.secret-key}")
//    private SecretKey secretKey;
//
//    public JWTTokenUtils(@Value("${spring.jwt.secret-key}")String secret){
//        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
//    }
//
//    @Value("${jwt.token.expired-time-ms}")
//    private Long expiredTimeMs;
//
//    public String getEmail(String token, SecretKey key) {
//        return extractClaims(token, key).get("email", String.class);
//    }
//    public String getRole(String token, String key) {
//        return extractClaims(token, key).get("role", String.class);
//    }
//
//    public static boolean isExpired(String token, String key) {
//        java.util.Date expiredDate = extractClaims(token, key).getExpiration();
//        return expiredDate.before(new java.util.Date());
//    }
//
//    private Claims extractClaims(String token, String key) {
//        return Jwts.parser()
//                .verifyWith(getKey(secretKey))
//                .build()
//                .parseSignedClaims(token)
//                .getPayload();
//    }
//
////    private SecretKey getKey(SecretKey key) {
////
////        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
////        return Keys.hmacShaKeyFor(keyBytes);
////    }
//
//
//    public String getCategory(String token) {
//        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category");
//    }
//    public String generatedToken(String category, String email, String role, long expiredTimeMs) {
//
//
//        return Jwts.builder()
//                .claim("category", category)
//                .claim("role",role)
//                .claim("email",email)
//                .subject(email)
//                .issuedAt(new Date(System.currentTimeMillis()))
//                .expiration(new Date(System.currentTimeMillis() + expiredTimeMs))
//                .signWith(getKey(secretKey))
//                .compact();
//    }
////    private Claims extractAllClaims(String token){
////        return Jwts.parser()
////                .verifyWith(getKey(key))
////                .build()
////                .parseSignedClaims(token)
////                .getPayload();
////    }
//
//}

