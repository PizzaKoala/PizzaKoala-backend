package com.PizzaKoala.Pizza.domain.oauth2.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final UserOAuth2Dto userOAuth2Dto;

    public CustomOAuth2User(UserOAuth2Dto userOAuth2Dto) {
        this.userOAuth2Dto = userOAuth2Dto;
    }
    @Override
    public Map<String, Object> getAttributes() {
        //google and naver가 주는 방식이 다르기 떄문에 나중에 확장을 위해 사용하지 않음
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userOAuth2Dto.getRole().toString();
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return userOAuth2Dto.getName();
    }
    public String getUsername() {
        return userOAuth2Dto.getUsername();
    }
    public String getEmail(){return userOAuth2Dto.getEmail();}
    public String getProfileImg() {
        return userOAuth2Dto.getProfileImg();
    }
}
