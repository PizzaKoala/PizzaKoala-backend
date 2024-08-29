package com.PizzaKoala.Pizza.global.config;

import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.PostRepository;
import com.PizzaKoala.Pizza.domain.entity.Images;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.Post;
import com.PizzaKoala.Pizza.domain.model.MemberRole;
import com.PizzaKoala.Pizza.domain.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ConfigUser {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostService postService;

    @PostConstruct
    public void init() {
        if (memberRepository.count() == 0) { // 이미 데이터가 있는지 확인
            Member member = Member.builder()
                    .email("Meep@kakao.com")
                    .nickName("MEEP")
                    .password(passwordEncoder.encode("123"))
                    .role(MemberRole.ADMIN)
                    .build();
            memberRepository.save(member);
            Member member2 = Member.builder()
                    .email("123@kakao.com")
                    .nickName("one")
                    .password(passwordEncoder.encode("123"))
                    .role(MemberRole.ADMIN)
                    .build();
            memberRepository.save(member2);

        }
    }
}
