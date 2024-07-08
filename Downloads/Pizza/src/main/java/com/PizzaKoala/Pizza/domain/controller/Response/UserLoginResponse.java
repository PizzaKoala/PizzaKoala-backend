package com.PizzaKoala.Pizza.domain.controller.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private String token;
//    private Long id;
//    private MemberRole role;
//    private String nickName;
//    private String email;
//    public static UserLoginResponse fromLoginUserDTO(LoginUserDTO member) {
//        return new UserLoginResponse(
//                member.getId(),
//                member.getRole(),
//                member.getNickName(),
//                member.getEmail()
//        );
//
//    }
}
