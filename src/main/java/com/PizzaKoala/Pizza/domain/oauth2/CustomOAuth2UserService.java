package com.PizzaKoala.Pizza.domain.oauth2;

import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.ProfileImageRepository;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.ProfileImage;
import com.PizzaKoala.Pizza.domain.model.MemberRole;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.domain.oauth2.dto.CustomOAuth2User;
import com.PizzaKoala.Pizza.domain.oauth2.dto.GoogleResponse;
import com.PizzaKoala.Pizza.domain.oauth2.dto.OAuth2Response;
import com.PizzaKoala.Pizza.domain.oauth2.dto.UserOAuth2Dto;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;


    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User);

        String registration = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registration.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {

            return null;
        }
//        //리소스 서버에서 발급 받은 정보로 사용자를 특정한 아이디값을 만듬.
//        String username = oAuth2Response.getProvider() +" "+oAuth2Response.getProviderId();

        Member existData = memberRepository.findByMyEmail(oAuth2Response.getEmail());

        /**
         *
         * nickname은 일단 이메일에 @ 앞부분을 따고 이미 존재하면 뒤에 숫자를 붙이장!! 케르륵
         *
         */

        String nickname = generateUniqueNickname(oAuth2Response.getEmail());
        //회원가입


       //회원가입
        if (existData == null) {
            Member member= Member.builder()
                    .email(oAuth2Response.getEmail())
                    .nickName(nickname)
                    .profileImageUrl(oAuth2Response.getPicture())
                    .role(MemberRole.USER)
                    .build();

            memberRepository.save(member);

            UserOAuth2Dto userOAuth2Dto= UserOAuth2Dto.builder()
                    .email(oAuth2Response.getEmail())
                    .picture(oAuth2Response.getPicture())
                    .nickname(nickname)
                    .role(MemberRole.USER)
                    .build();
            return new CustomOAuth2User(userOAuth2Dto);
        } else { //로그임
            //TODO: 구글 로그인떄 이름(nickname), 사진이 바뀌었을떄 업데이트하는 부분 만들어야한다.
//            existData.setEmail(oAuth2Response.getEmail());
//            existData.setName(oAuth2Response.getName());
//
//            userRepository.save(existData);


            UserOAuth2Dto userOAuth2Dto= UserOAuth2Dto.builder()
                    .nickname(nickname)
                    .picture(oAuth2Response.getPicture())
                    .email(existData.getEmail())
                    .role(existData.getRole())
                    .build();


            return new CustomOAuth2User(userOAuth2Dto);

        }//모지

    }

    private String generateUniqueNickname(String email) {
            String nickname = email.split("@")[0];
            String uniqueNickname = nickname;
            int count=1;
            while (memberRepository.existsByNickName(nickname)) {
                uniqueNickname = nickname+count;
                count++;
            }
            return uniqueNickname;

    }

}
