package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.request.UserJoinRequest;
import com.PizzaKoala.Pizza.domain.controller.request.UserLoginRequest;
import com.PizzaKoala.Pizza.domain.controller.response.ErrorResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.response.UserJoinResponse;
import com.PizzaKoala.Pizza.domain.controller.swagInterface.MemberControllerDoc;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import com.PizzaKoala.Pizza.domain.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController implements MemberControllerDoc {
    private final MemberService memberService;

    @PostMapping(value = "/join", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Response<UserJoinResponse> join(@RequestPart("file") MultipartFile file,
                                           @RequestPart("request") UserJoinRequest request, HttpServletResponse response) throws IOException {

        UserDTO joinUserDTO = memberService.join(file, request.getNickName(), request.getEmail(), request.getPassword(), response);
        return Response.success(UserJoinResponse.fromJoinUserDTO(joinUserDTO));

    }

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Response<Void> login(@org.springframework.web.bind.annotation.RequestBody UserLoginRequest request) {
        memberService.login(request.getEmail(), request.getPassword());
        return Response.success(null);
    }


}
