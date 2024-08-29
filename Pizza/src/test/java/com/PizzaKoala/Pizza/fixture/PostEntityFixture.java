package com.PizzaKoala.Pizza.fixture;

import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.Post;

public class PostEntityFixture {
    public static Post get(String email, Long postId,Long id) {
        Member result = Member.builder()
                .id(id)
                .email(email)
                .build();
        return Post.builder()
                .member(result)
                .id(postId)
                .build();
    }
//    public static Member getSignUp(String nickname,String email, String password) {
//        Member result = Member.builder()
//                .id(1L)
//                .nickName(nickname)
//                .password(password)
//                .email(email)
//                .build();
//        return result;
//    }
}
