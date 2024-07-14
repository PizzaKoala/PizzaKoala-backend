package com.PizzaKoala.Pizza.service;

import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import com.PizzaKoala.Pizza.fixture.MemberEntityFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void 회원가입이_정상적으로_동작하는_경우() {
        String nickName = "nickName";
        String password = "password";
        String email = "email";
        Long id=1L;
        MockMultipartFile files =
                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes());


        Member fixture = MemberEntityFixture.getSignUp(nickName,email, password,id);


        // mocking - thenReturn(Optional.empty())이건 디비에 그 이메일이 없다고 리턴하게 하는 코드
        //이메일 존재 검증
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        //닉네임 존재 검증
        when(memberRepository.findByNickName(nickName)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encrypt_password");
        when(memberRepository.save(any())).thenReturn(MemberEntityFixture.getSignUp(nickName, email, password,id));
        Assertions.assertDoesNotThrow(() -> memberService.join(files,nickName, password, email));
    }
    @Test
    void 회원가입시_이미_닉네임이_존재하는_경우() {
        String nickName = "nickName";
        String password = "password";
        String email = "email";
        Long id=1L;
        MockMultipartFile files =
                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes());

        Member fixture = MemberEntityFixture.getSignUp(nickName,email, password,id);

        // mocking - thenReturn(Optional.empty())이건 디비에 그 이메일이 없다고 리턴하게 하는 코드
        //이메일 존재 검증
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encrypt_password");

        //닉네임 존재 검증
        when(memberRepository.findByNickName(nickName)).thenReturn(Optional.of(fixture));
        when(memberRepository.save(any())).thenReturn(Optional.of(fixture));
        PizzaAppException e = Assertions.assertThrows(PizzaAppException.class, () -> memberService.join(files,nickName, email,password));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.DUPLICATED_NICKNAME);
    }
    @Test
    void 회원가입시_이미_email이_존재하는_경우_에러반황() {
        String nickName = "nickName";
        String password = "password";
        String email = "email";
        Long id=1L;

        MockMultipartFile files =
                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes());

        Member fixture = MemberEntityFixture.getSignUp(nickName,email, password,id);

        // mocking - thenReturn(Optional.empty())이건 디비에 그 이메일이 없다고 리턴하게 하는 코드
        //이메일 존재 검증
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(fixture));
        when(passwordEncoder.encode(password)).thenReturn("encrypt_password");
        //닉네임 존재 검증
        when(memberRepository.findByNickName(nickName)).thenReturn(Optional.empty());
        when(memberRepository.save(any())).thenReturn(Optional.of(mock(Member.class)));
        PizzaAppException e = Assertions.assertThrows(PizzaAppException.class, () -> memberService.join(files,nickName,email, password));
        Assertions.assertEquals(e.getErrorCode(),ErrorCode.DUPLICATED_EMAIL_ADDRESS);
    }
    @Test
    void login이_정상적으로_동작하는_경우() {
        String password = "password";
        String email = "email";
        Long id=1L;
        Member fixture = MemberEntityFixture.getLogin(email, password,id);

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(fixture));
        when(passwordEncoder.matches(password, fixture.getPassword())).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> memberService.login(email,password));
    }
    @Test
    void login시_email이_존재하지_않는_경우() {
        String password = "password";
        String email = "email";

        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        PizzaAppException e = Assertions.assertThrows(PizzaAppException.class, () -> memberService.login(password, email));
        Assertions.assertEquals(e.getErrorCode(),ErrorCode.MEMBER_NOT_FOUND);
    }
    @Test
    void login시_비밀번호가_틀린_경우() {
        String password = "password";
        String wrongPassword = "Wrongpassword";
        String email = "email";
        Long id=1L;

        Member fixture = MemberEntityFixture.getLogin(email, password,1l);

        //email 확인
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(fixture));

        PizzaAppException e = Assertions.assertThrows(PizzaAppException.class, () -> memberService.login(email,wrongPassword));
        Assertions.assertEquals(e.getErrorCode(),ErrorCode.INVALID_PASSWORD);
    }

}
