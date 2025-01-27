package com.PizzaKoala.Pizza.global.config;

import com.PizzaKoala.Pizza.domain.Repository.ImageRepository;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.PostRepository;
import com.PizzaKoala.Pizza.domain.controller.PostController;
import com.PizzaKoala.Pizza.domain.entity.Images;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.Post;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.model.MemberRole;
import com.PizzaKoala.Pizza.domain.service.FollowService;
import com.PizzaKoala.Pizza.domain.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ConfigUser {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final FollowService followService;
    private final PostService postService;

    @PostConstruct
    public void init() {
        if (memberRepository.count() == 0) { // 이미 데이터가 있는지 확인
            //member1

            /**
             * email- Meep@kakao.com
             * password- 123
             */
            Member member = Member.builder()
                    .email("Meep@kakao.com")
                    .profileImageUrl("https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-12-21-27-13-2.jpeg")
                    .nickName("MEEP")
                    .password(passwordEncoder.encode("123"))
                    .role(MemberRole.ADMIN)
                    .build();
            Member member_1=memberRepository.save(member);

            //member1의 포스트1
            String title = "What a meepy day.";
            String desc = "피곤하고 정신없는 하루였다. ";
            Post post=postRepository.save(Post.of(title,desc,member));
            Images image = Images.of(post, member.getId(),
                    "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/47896696-f511-4697-b3f5-2a7b194a3dbc.png",
                    "image/jpeg", 322771L,"KakaoTalk_Photo_2024-04-30-16-57-50.png" );
            imageRepository.save(image);
            Images image2 = Images.of(post, member.getId(),
                    "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/62d9ad5c-6df8-473d-b88f-a923adcade3d.jpeg",
                    "image/jpeg", 322771L,"KakaoTalk.png" );
            imageRepository.save(image2);
            //member1의 포스트2
            String title1_2 = "애민이";
            String desc1_2 = "애민이는 무럭무럭 잘자라고 있다. 초로기 예쁨.";
            Post post1_2=postRepository.save(Post.of(title1_2,desc1_2,member));
            Images image1_2 = Images.of(post1_2, member.getId(),
                    "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-06-17-22-15-23.jpeg",
                    "image/jpeg", 137000L,"KakaoTalk_Photo_2024-06-17-22-15-23" );
            imageRepository.save(image1_2);
            //member1의 포스트3
            String title1_3 = "박물관 다녀온 날";
            String desc1_3 = "박물관 굿즈들 구경하는 재미가 있다. 아기 용가리들도 너무 귀여웠다. ";
            Post post1_3=postRepository.save(Post.of(title1_3,desc1_3,member));
            Images image1_3 = Images.of(post1_3, member.getId(),
                    "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-57.jpeg",
                    "image/jpeg", 137000L,"KakaoTalk_Photo_2024-06" );
            imageRepository.save(image1_3);

/**
 * member- monster
 * email- 222@Kakao.com
 * password- 222
 */
            //member2
            Member member2 = Member.builder()
                    .email("222@kakao.com")
                    .nickName("monster")
                    .password(passwordEncoder.encode("222"))
                    .profileImageUrl("https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-09-03-14-38-37.jpeg")
                    .role(MemberRole.USER)
                    .build();
            Member member_2= memberRepository.save(member2);
          
            //member2의 포스트1
            String title2 = "점심";
            String desc2 = "보쌈 너무 맛있당... Monster 뚜비와 함께..!";
            Post post2=postRepository.save(Post.of(title2,desc2,member2));
            Images image2_1 = Images.of(post2, member2.getId(),
                    "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-01-20-15-03-43.jpeg",
                    "image/jpeg", 278600L,"KakaoTalk.png" );
            imageRepository.save(image2_1);


/**
 * member- meh
 * email- 333@Kakao.com
 * password- 333
 */

            //member3
            Member member3 = Member.builder()
                    .email("333@kakao.com")
                    .nickName("meh")
                    .profileImageUrl("https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2023-11-05-00-46-13.jpeg")
                    .password(passwordEncoder.encode("333"))
                    .role(MemberRole.USER)
                    .build();
            Member member_3=memberRepository.save(member3);
            //member2의 포스트1
            String title3 = "파스스";
            String desc3 = "meh... 오늘도 불태웠다.. 인증 완료....";
            postRepository.save(Post.of(title3,desc3,member3));
            Images image3 = Images.of(post, member3.getId(),
                    "https://pizzakoala.s3.ap-northeast-2.amazonaws.com/KakaoTalk_Photo_2024-04-26-16-19-35.jpeg",
                    "image/jpeg", 278600L,"KakaoTalk.png" );
            imageRepository.save(image3);


/**
 * member- miracle
 * email- 444@Kakao.com
 * password- 444
 */

            Member member4 = Member.builder()
                    .email("444@kakao.com")
                    .nickName("miracle")
                    .password(passwordEncoder.encode("444"))
                    .role(MemberRole.USER)
                    .build();
            Member member_4=memberRepository.save(member4);
/**
 * Follow
 */
            followService.follow("Meep@kakao.com", member_2.getId());
            followService.follow("Meep@kakao.com", member_3.getId());

            followService.follow("222@kakao.com", member_1.getId());
            followService.follow("222@kakao.com", member_3.getId());

            followService.follow("333@kakao.com", member_1.getId());
            followService.follow("333@kakao.com", member_2.getId());

            followService.follow("444@kakao.com", member_1.getId());
            followService.follow("444@kakao.com", member_3.getId());

/**
 * likes
 */
            postService.likes(post.getId(),"222@kakao.com");
            postService.likes(post.getId(),"333@kakao.com");
            postService.likes(post.getId(),"444@kakao.com");


            postService.likes(post1_2.getId(),"222@kakao.com");
            postService.likes(post1_2.getId(),"333@kakao.com");

            postService.likes(post1_3.getId(),"444@kakao.com");

            postService.likes(5L,"222@kakao.com");
            postService.likes(5L,"Meep@kakao.com");
            postService.likes(5L,"444@kakao.com");


            /**
             * comments
             */
            postService.comment(post.getId(),"Meep@kakao.com","내가 쓴 나의 게시글이당");
            postService.comment(4L,"Meep@kakao.com","안녕!!!!");
            postService.comment(post.getId(),"222@kakao.com","코알라당!!");
            postService.comment(post.getId(),"333@kakao.com","이게 뭐얌!");
            postService.comment(post.getId(),"444@kakao.com",">_<");
        }

    }
}




