package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.RefreshRepository;
import com.PizzaKoala.Pizza.domain.Util.JWTTokenUtils;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.model.MemberRole;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class JWTService {
    //TODO: error 처리, expiredMS 하나로 만들기.
    // -redis는 자동으로 리프레쉬 토큰 삭제해주는데 다른 디비는 계속 쌓여서 용량문제가 일어나서 주기적으로 지난 토큰을 삭제해주는게 좋다.
    private final JWTTokenUtils jwtTokenUtils;
    private final RefreshRepository refreshRepository;

    public JWTService(JWTTokenUtils jwtTokenUtils, RefreshRepository refreshRepository) {
        this.jwtTokenUtils= jwtTokenUtils;
        this.refreshRepository= refreshRepository;
    }

    public ResponseEntity<?> reissueToken(Cookie[] cookies, HttpServletResponse response) {

        //get refresh token
        String refresh = null;

        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {

                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtTokenUtils.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtTokenUtils.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }
        String username = jwtTokenUtils.getUsername(refresh);

        //DB에 해당하는 refresh token 이 있는지 확인하기.
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);

        }

        String role = jwtTokenUtils.getRole(refresh);

        //make new JWT
        String newAccess = jwtTokenUtils.generatedToken("access", username, role, 600000L);
        String newRefresh = jwtTokenUtils.generatedToken("refresh", username, role,  86400000L); //24hours life cycle

        //db에 새로운 리프레쉬 토큰 저장하고 기존에 있던 리프레쉬 도큰 삭제하는 메서드
        updateNewRefreshToDbAndDeleteOldRefresh(refresh,username,newRefresh,86400000L);

        //response
        response.setHeader("access", newAccess);
        response.addCookie(jwtTokenUtils.createCookie("refresh",newRefresh));



        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateNewRefreshToDbAndDeleteOldRefresh(String refresh,String email, String newRefresh, Long expiredTimeMs) {
        refreshRepository.deleteByRefresh(refresh);
        jwtTokenUtils.addRefreshEntity(email,newRefresh, expiredTimeMs);
    }

}
