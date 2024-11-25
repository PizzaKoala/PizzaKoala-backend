package com.PizzaKoala.Pizza.fixture;

import com.PizzaKoala.Pizza.member.entity.Member;

public class MemberEntityFixture {
    public static Member getLogin(String email, String password,Long id) {
        Member result = Member.builder()
                .id(id)
                .password(password)
                .email(email)
                .build();
        return result;
    }
    public static Member getSignUp(String nickname,String email, String password, Long id) {
        Member result = Member.builder()
                .id(id)
                .nickName(nickname)
                .password(password)
                .email(email)
                .build();
        return result;
    }
}
