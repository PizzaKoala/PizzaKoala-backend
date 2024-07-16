//package com.PizzaKoala.Pizza.controller;
//
//import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
//import com.PizzaKoala.Pizza.domain.controller.request.UserJoinRequest;
//import com.PizzaKoala.Pizza.domain.controller.request.UserLoginRequest;
//import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
//import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
//import com.PizzaKoala.Pizza.domain.model.UserDTO;
//import com.PizzaKoala.Pizza.domain.service.MemberService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithAnonymousUser;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.awt.print.Pageable;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class MemberControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private MemberService memberService;
//
//    @MockBean
//    private MemberRepository memberRepository;
//
//    @MockBean
//    private PasswordEncoder passwordEncoder;
//
////
////    @Test
////    public void 회원가입() throws Exception {
////        String password = "password";
////        String nickname = "nickname";
////        String email = "email";
////        MockMultipartFile files =
////                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes());
////
////
////        when(memberService.join(files,nickname,email,password)).thenReturn(mock(UserDTO.class));
////
////        mockMvc.perform(post("/api/v1/join")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        //TODO : add request body
////                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(nickname,email,password,files)))
////                ).andDo(print())
////                .andExpect(status().isOk());
////    }
//
////    @Test
////    public void 회원가입시_이미_회원_가입된_userName일_경우_에러반환() throws Exception {
////        String nickname = "username";
////        String password = "password";
////        String email = "email";
////        MockMultipartFile files =
////                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes());
////        when(memberService.join(files,nickname,email,password)).thenThrow(new PizzaAppException(ErrorCode.DUPLICATED_EMAIL_ADDRESS,""));
////
////        mockMvc.perform(post("/api/v1/join")
////                    .contentType(MediaType.APPLICATION_JSON)
////                    //TODO : add request body
////                    .content(objectMapper.writeValueAsBytes(new UserJoinRequest(nickname,email, password,files)))
////                ).andDo(print())
////                .andExpect(status().isConflict());
////    }
//
////    @Test
////    public void 로그인() throws Exception {
////        String email = "email";
////        String password = "password";
////
////        when(memberService.login(email,password)).thenReturn("test_token");
////        mockMvc.perform(post("/api/v1/login")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        //TODO : add request body
////                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(email, password)))
////                ).andDo(print())
////                .andExpect(status().isOk());
////    }
//    @Test
//    public void 로그인실패_등록되지않은_이메일일_경우_에러반환() throws Exception {
//        String email = "emdsdail";
//        String password = "pdssw";
//
//        when(memberService.login(email, password)).thenThrow(new PizzaAppException(ErrorCode.MEMBER_NOT_FOUND));
//
//        mockMvc.perform(post("/api/v1/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        //TODO : add request body
//                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(email, password)))
//                ).andDo(print())
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    public void 로그인실패_비밀번호가_틀렸을때_에러반환() throws Exception {
//        String email = "email";
//        String password = "password";
//
//        when(memberService.login(email, password)).thenThrow(new PizzaAppException(ErrorCode.INVALID_PASSWORD));
//
//        mockMvc.perform(post("/api/v1/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        //TODO : add request body
//                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(email, password)))
//                ).andDo(print())
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @WithMockUser
//    public void 알람기능() throws Exception{
//        when(memberService.alarmList(any(),any())).thenReturn(Page.empty());
//        mockMvc.perform(get("/api/v1/alarm")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @WithAnonymousUser
//    public void 알람리스트_요청시_로그인_하지않은_경우() throws Exception{
//        when(memberService.alarmList(any(),any())).thenReturn(Page.empty());
//        mockMvc.perform(get("/api/v1/alarm")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//    }
//
//}
