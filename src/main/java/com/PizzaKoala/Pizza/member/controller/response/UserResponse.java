package com.PizzaKoala.Pizza.member.controller.response;

import com.PizzaKoala.Pizza.member.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {
    private Long id;
    private String email;
    private String profileImageUrl;
    private String nickname;

    public static UserResponse fromUser(UserDTO user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getProfileImageUrl(),
                user.getNickName()
        );
    }
}

