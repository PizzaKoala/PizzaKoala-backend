package com.PizzaKoala.Pizza.controller;

import com.PizzaKoala.Pizza.domain.controller.request.PostCreateRequest;
import com.PizzaKoala.Pizza.domain.controller.request.PostModifyRequest;
import com.PizzaKoala.Pizza.domain.controller.request.UserJoinRequest;
import com.PizzaKoala.Pizza.domain.exception.ErrorCode;
import com.PizzaKoala.Pizza.domain.exception.PizzaAppException;
import com.PizzaKoala.Pizza.domain.service.PostService;
import com.PizzaKoala.Pizza.fixture.PostEntityFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Arrays;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonpath.internal.function.ParamType.JSON;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.PUT;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostService postService;
    String title = "title";
    String desc = "body";


    /**
     *
     * 포스트 작성
     *
     */


    @Test
    @WithMockUser //로그인 된경우
    void 포스트작성_성공() throws Exception {

        String accessToken = "access_token";
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.POST,"/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new PostCreateRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isOk());
//        // Create an array of mock files
//        MockMultipartFile[] files = {
//                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes()),
//                new MockMultipartFile("files", "meepyday2.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 2".getBytes()),
//                new MockMultipartFile("files", "meepyday3.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 3".getBytes()),
//                new MockMultipartFile("files", "meepyday4.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 4".getBytes()),
//                new MockMultipartFile("files", "meepyday5.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 5".getBytes())
//        };
//        String accessToken = "access_token";
//        mockMvc.perform(MockMvcRequestBuilders
//                .multipart(HttpMethod.POST,"/api/*/posts")
//                        .file(files[0])
//                        .file(files[1])
//                        .file(files[2])
//                        .file(files[3])
//                        .file(files[4])
//                        .param("title",title)
//                        .param("desc",desc)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .header("Authorization", accessToken)
//        ).andDo(print())
//                .andExpect(status().isOk());
    }
    @Test
    @WithAnonymousUser //로그인 하지 않은 경우
    void 포스트작성시_로그인하지_않은경우() throws Exception {
        String title = "title";
        String desc = "body";

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
//        (ErrorCode.INVALID_TOKEN.getStatus().value()));


        // Create an array of mock files
//        MockMultipartFile[] files = {
//                new MockMultipartFile("files", "meepyday1.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 1".getBytes()),
//                new MockMultipartFile("files", "meepyday2.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 2".getBytes()),
//                new MockMultipartFile("files", "meepyday3.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 3".getBytes()),
//                new MockMultipartFile("files", "meepyday4.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 4".getBytes()),
//                new MockMultipartFile("files", "meepyday5.jpg", MediaType.IMAGE_JPEG_VALUE, "file content 5".getBytes())
//        };

//         .file(files[0])
//                .file(files[1])
//                .file(files[2])
//                .file(files[3])
//                .file(files[4])
//        String accessToken = "access_token";
//        mockMvc.perform(MockMvcRequestBuilders
//                        .multipart(HttpMethod.POST,"/api/*/posts")
//                        .param("title",title)
//                        .param("desc",desc)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.MULTIPART_FORM_DATA)
//                        .header("Authorization", accessToken)
//                ).andDo(print())
//                .andExpect(status().isUnauthorized());
    }

    /**
     *
     * 포스트 수정
     *
     */

//    @Test
//    @WithMockUser
//    void 포스트수정() throws Exception {
//        String email = "email";
//        String title = "Meepyday";
//        String desc = "meep!";
//        Long postId= 1L;
//        when(postService.modify(eq(title),eq(desc),any(),any()))
//                .thenReturn(PostModifyDTO.fromPostEntity(PostEntityFixture.get(email,postId,postId)));
//
//        mockMvc.perform(put("/api/v1/posts/1")
//                        .header("Authorization","Bearer mock-jwt-token")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, desc))))
//                .andDo(print())
//                .andExpect(status().isOk());
//    }



    @Test
    @WithAnonymousUser
    void 포스트수정시_로그인하지않은_경우() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT,"/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    void 포스트수정시_본인이_작성한_글이_아닐떄_에러발생() throws Exception {

        doThrow(new PizzaAppException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title),eq(desc),any(),eq(1L));

        String accessToken = "access_token";
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT,"/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    void 포스트수정시_수정하려는_글이_없는_경우() throws Exception {

        doThrow(new PizzaAppException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title),eq(desc),any(),eq(1L));

        String accessToken = "access_token";
        mockMvc.perform(MockMvcRequestBuilders.multipart(HttpMethod.PUT,"/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest("title", "body"))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     *
     * 포스트 삭제
     *
     */


    @Test
    @WithMockUser
    void 포스트삭제() throws Exception {

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 포스트_삭제시_로그인하지_않은_경우() throws Exception {

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 포스트삭제시_작성자와_삭제요청자가_다를_경우() throws Exception {

        doThrow(new PizzaAppException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(),any());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void 삭제할_포스트가_존재하지_않는_경우() throws Exception {

        doThrow(new PizzaAppException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(),any());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    /**
     *
     *
     * 피드-  최신-posts,좋아요순 most-liked, 내 페이지(최신)-my page
     *
     */
    @Test
    @WithMockUser
    void 최신_피드() throws Exception {



        mockMvc.perform(get("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 최신_피드요청시_로그인_하지_않은_경우() throws Exception {

        mockMvc.perform(delete("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    /** 내피드목록
     */
    @Test
    @WithMockUser
    void 내피드목록() throws Exception {



        mockMvc.perform(get("/api/v1/posts/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void 내피드요청시_로그인_하지_않은_경우() throws Exception {

        mockMvc.perform(delete("/api/v1/posts/my")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
