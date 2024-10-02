package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.AlarmRepository;
import com.PizzaKoala.Pizza.domain.Repository.FollowRepository;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.ProfileImageRepository;
import com.PizzaKoala.Pizza.domain.Util.JWTTokenUtils;
import com.PizzaKoala.Pizza.domain.entity.Follow;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.ProfileImage;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;

import com.PizzaKoala.Pizza.domain.model.AlarmDTO;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.global.config.filter.NewLoginFilter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AlarmRepository alarmRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final ProfileImageRepository profileImageRepository;
    private final AuthenticationService authenticationService;
    private final FollowRepository followRepository;

    @Transactional //exception     발생할 경우 롤백되어서 저장되지 않는다
    public UserDTO join(MultipartFile file,String nickName, String email, String password, HttpServletResponse response) throws IOException { //email, nickname, password, profile-photo

        //이메일이 이미 가입되어있는지
        memberRepository.findByEmail(email).ifPresent(it -> {
            throw new PizzaAppException(ErrorCode.DUPLICATED_EMAIL_ADDRESS, String.format("%s is duplicated", email));
        });

        //닉네임 존재여부
        memberRepository.findByNickName(nickName).ifPresent(it -> {
            throw new PizzaAppException(ErrorCode.DUPLICATED_NICKNAME, String.format("%s is duplicated", nickName));
        });

        //TODO 사진 프로필 올리기
        /*s3 이미지 업로드 로직*/
        String profileImage = s3ImageUploadService.upload(file);

        Member member = Member.of(nickName, email, passwordEncoder.encode(password), profileImage);

        // 회원가입 등록
        Member result = memberRepository.save(member);
//        throw new PizzaAppException(ErrorCode.DUPLICATED_EMAIL_ADDRESS, String.format("%is Duplicated", email));


        /* db에 저장*/
        try {
            ProfileImage profile = ProfileImage.of(result.getId(), profileImage, file.getContentType(), file.getSize(), file.getOriginalFilename());
            profileImageRepository.save(profile);
        } catch (Exception e) {
            s3ImageUploadService.deleteFiles(profileImage);
            memberRepository.delete(member);
            throw new PizzaAppException(ErrorCode.IMAGE_UPLOAD_REQUIRED);
        }
        //jwt 생성
        authenticationService.handleSuccessfulAuthentication(member.getEmail(),member.getRole().toString(),response);
        return UserDTO.fromMemberEntity(result);
    }


    public Void login(String email, String password) {
        //회원가입 이메일인지 체크
        Member member=memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded ",email)));

        //비밀번호 체크
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new PizzaAppException(ErrorCode.INVALID_PASSWORD);
        }
        return null;
    }



    public Page<AlarmDTO> alarmList(String email, Pageable pageable) {
//        //회원가입 이메일인지 체크
        Member member=memberRepository.findByEmail(email).orElseThrow(()-> new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND, String.format("%s is not founded ",email)));
        Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        return alarmRepository.findAllByMemberId(pageable1,member.getId()).map(AlarmDTO::fromAlarmEntity);

    }


}
