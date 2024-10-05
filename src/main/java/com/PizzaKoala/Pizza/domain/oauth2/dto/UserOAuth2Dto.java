package com.PizzaKoala.Pizza.domain.oauth2.dto;

import com.PizzaKoala.Pizza.domain.model.MemberRole;
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
}
