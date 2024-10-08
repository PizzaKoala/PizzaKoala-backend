package com.PizzaKoala.Pizza.domain.controller.response;
import com.PizzaKoala.Pizza.domain.model.MemberRole;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

    private Long id;
    private String email;
    private MemberRole role;
    private String profileImageUrl;


    public static UserJoinResponse fromJoinUserDTO(UserDTO member) {
        return new UserJoinResponse(
                member.getId(),
                member.getEmail(),
                member.getRole(),
                member.getProfileImageUrl()
        );

    }

}
