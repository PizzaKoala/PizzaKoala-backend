package com.PizzaKoala.Pizza.domain.controller.Response;

import com.PizzaKoala.Pizza.domain.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponse {
    private Long id;
    private String email;

    public static UserResponse fromUser(UserDTO user) {
        return new UserResponse(
                user.getId(),
                user.getEmail()
        );
    }
}

