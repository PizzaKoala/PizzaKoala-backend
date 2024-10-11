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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;
//마지막 작업- 로그인부분
// 리소스 서버에서 CustomOAuth2UserService로 정보를 받아서 userOAuth2Dto라는 dto에 담아서 앞단인 provider에 넘겨주면 로그인이 진행된다.
//
//    보편적인 방식: 이메일 중복을 막고 계정 통합
//    대부분의 경우, 소셜 로그인과 기본 로그인의 이메일은 중복되지 않도록 설계하며,
//    통합 관리를 하는 편입니다. 이 방식은 중복 계정 문제를 방지하고, 더 나은 사용자 경험을 제공합니다.
//    만약 사용자가 소셜 로그인 후 기본 로그인을 원할 경우, 소셜 계정과 연결된 비밀번호 설정 절차(패스워드 설정)를
//    통해 기본 로그인으로 전환할 수 있게 할 수도 있습니다.

    /**
     * 일단은 통합로그인 안하고 기본이나 소셜 둘중 하나만 가능하게 구현되어있다.
     */
    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //구글에서 온건지 네이버에서 온건지 확인
        String registration = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registration.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            return null;
        }
//        String username = "";
//        if (oAuth2Response.getUsername().isEmpty() || oAuth2Response.getUsername() == null) {
//            username = "";
//        } else {
//            username = oAuth2Response.getUsername();
//        }
//        Member existData = memberRepository.findBySocialLoginUsername(username);

        /**
         * nickname은 일단 이메일에 @ 앞부분을 따고 이미 존재하면 뒤에 숫자를 붙이장!!ㅌ
         */

//        String nickname = generateUniqueNickname(oAuth2Response.getEmail());
        String googleUsername = oAuth2Response.getProvider() +" "+oAuth2Response.getProviderId();

        //회원가입
        Member existData = memberRepository.findBySocialLoginUsername(googleUsername);
//        Member existData = memberRepository.findByMyEmail(oAuth2Response.getEmail());


       //회원가입
        if (existData == null) {
            String nickname = generateUniqueNickname(oAuth2Response.getEmail());
            //        //리소스 서버에서 발급 받은 정보로 사용자를 특정한 아이디값을 만듬.- 이메일로 찾기 때문에 필요없는거 같지만 일단 만들어두자.
            Member member = Member.googleLoginMember(nickname, oAuth2Response.getEmail(), oAuth2Response.getPicture(),googleUsername);

           memberRepository.save(member);
            /**
             * socialusername이 이미 디비에 존재하는지는 아직 체크 안해줬다.
             */

            UserOAuth2Dto userOAuth2Dto = UserOAuth2Dto.builder()
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .username(nickname)
                    .role(MemberRole.USER)
                    .build();
            return new CustomOAuth2User(userOAuth2Dto);
        } else { //로그임
            //TODO: 구글 로그인떄 이름(nickname), 사진이 바뀌었을떄 업데이트하는 부분 만들어야한다.

            existData.updateGoogleInfo_email(oAuth2Response.getEmail());
            Member member=memberRepository.save(existData);
            UserOAuth2Dto userOAuth2Dto = UserOAuth2Dto.builder()
                    .name(oAuth2Response.getName())
                    .username(member.getNickName())
                    .email(oAuth2Response.getEmail())
                    .role(MemberRole.USER)
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
                log.info(uniqueNickname);
                count++;
            }
            return uniqueNickname;

    }

}
