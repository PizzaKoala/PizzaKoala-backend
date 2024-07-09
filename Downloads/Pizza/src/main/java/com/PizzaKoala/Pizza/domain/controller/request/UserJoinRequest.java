package com.PizzaKoala.Pizza.domain.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class UserJoinRequest {
    private String nickName;
    private String email;
    private String password;
}