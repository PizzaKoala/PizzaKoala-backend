package com.PizzaKoala.Pizza.global.config;

import com.PizzaKoala.Pizza.domain.Repository.ImageRepository;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.PostRepository;
import com.PizzaKoala.Pizza.domain.controller.PostController;
import com.PizzaKoala.Pizza.domain.entity.Images;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.Post;
import com.PizzaKoala.Pizza.domain.model.MemberRole;
import com.PizzaKoala.Pizza.domain.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ConfigUser {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final PostService postService;
    private final ImageRepository imageRepository;

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
            Member member3 = Member.builder()
                    .email("three@kakao.com")
                    .nickName("three")
                    .password(passwordEncoder.encode("333"))
                    .role(MemberRole.USER)
                    .build();
            memberRepository.save(member3);
            Member member4 = Member.builder()
                    .email("four@kakao.com")
                    .nickName("four")
                    .password(passwordEncoder.encode("444"))
                    .role(MemberRole.USER)
                    .build();
            memberRepository.save(member4);
//
//            //meep@kakao.com 의 첫번쨰 포스트+ 이미지
//            Post post=postRepository.save(Post.of("Meepy_day", "I had a meepy day.",member));


//            Images images1_1 = Images.builder()
//                    .url("https://pizzakoala.s3.ap-northeast-2.amazonaws.com/02391593-cc16-470f-bd20-c354f2f17be6.jpeg")
//                    .memberId(member.getId())
//                    .postId(post)
//                    .build();
//            imageRepository.save(images1_1);
//            Images images1_2 = Images.builder()
//                    .url("https://pizzakoala.s3.ap-northeast-2.amazonaws.com/02bdfda9-708c-4064-bd9e-a6c325fd8d3d.jpeg")
//                    .memberId(member.getId())
//                    .postId(post)
//                    .build();
//            imageRepository.save(images1_2);
//            //meep@kakao.com 의 두번쨰 포스트+ 이미지
//            Post post1_2 = Post.builder()
//                    .id(member.getId())
//                    .likes(1L)
//                    .title("Phew")
//                    .desc("I dont like humans tsk tsk")
//                    .build();
//            Images images2_1 = Images.builder()
//                    .url("https://pizzakoala.s3.ap-northeast-2.amazonaws.com/15ad67e8-61c4-4bd2-9813-1560b1346431.jpeg")
//                    .memberId(member.getId())
//                    .postId(post1_1)
//                    .build();
//            postRepository.save(post1_2);
//            imageRepository.save(images2_1);
        }
    }
}




