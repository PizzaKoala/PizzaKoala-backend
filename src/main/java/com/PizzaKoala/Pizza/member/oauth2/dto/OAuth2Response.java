package com.PizzaKoala.Pizza.member.oauth2.dto;

public interface OAuth2Response {
    //제공자(Ex. naver, google)
    String getProvider();
    //제공자에서 발급해주는 아이디 번호
    String  getProviderId();
    //이메일
    String getEmail();

    String getPicture();

    String getName();
    String getUsername();
}
