package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.AlarmRepository;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Util.JWTTokenUtils;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;

import com.PizzaKoala.Pizza.domain.model.AlarmDTO;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AlarmRepository alarmRepository;

//    @Value("${jwt.secret-key}")
//    private String secretKey;
//
//    @Value("${jwt.token.expired-time-ms}")
//    private Long expiredTimeMs;
    @Transactional //exception     발생할 경우 롤백되어서 저장되지 않는다
    public UserDTO join(String nickName, String email, String password) { //email, nickname, password, profile-photo
        //TODO
//        , MultipartFile file
        //사진 프로필 올리기


        //이메일이 이미 가입되어있는지
        memberRepository.findByEmail(email).ifPresent(it -> {
            throw new PizzaAppException(ErrorCode.DUPLICATED_EMAIL_ADDRESS, String.format("%s is duplicated", email));
        });

        //닉네임 존재여부
        memberRepository.findByNickName(nickName).ifPresent(it -> {
            throw new PizzaAppException(ErrorCode.DUPLICATED_NICKNAME, String.format("%s is duplicated", nickName));
        });
        Member member = Member.of(nickName, email, passwordEncoder.encode(password));

        // 회원가입 등록
        Member result = memberRepository.save(member);
//        throw new PizzaAppException(ErrorCode.DUPLICATED_EMAIL_ADDRESS, String.format("%is Duplicated", email));
        return UserDTO.fromMemberEntity(result);
    }


    public Void login(String email, String password) {
        //회원가입 이메일인지 체크
        Member member=memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded ",email)));

        //비밀번호 체크
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new PizzaAppException(ErrorCode.INVALID_PASSWORD);
        }


        //토큰생성
//        return JWTTokenUtils.generatedToken(email, secretKey, expiredTimeMs);
        return null;
    }

    public Member loadMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(()->
                new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s nt found", email)));
    }

    public Page<AlarmDTO> alarmList(String email, Pageable pageable) {
        //회원가입 이메일인지 체크
        Member member=memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded ",email)));

        return alarmRepository.findAllByMemberId(member.getId(),pageable).map(alarm -> AlarmDTO.fromAlarmEntity(alarm,member));

    }
}