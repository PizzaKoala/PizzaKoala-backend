package com.PizzaKoala.Pizza.member.oauth2.dto;

import com.PizzaKoala.Pizza.member.entity.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class UserOAuth2Dto {
    private String name;
    private MemberRole role;
    private String email;
    private String username;
    private String picture;
    private String socialLoginUsername;
}
