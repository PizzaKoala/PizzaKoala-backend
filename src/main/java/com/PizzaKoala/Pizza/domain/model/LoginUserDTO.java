package com.PizzaKoala.Pizza.domain.model;

import com.PizzaKoala.Pizza.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LoginUserDTO {
    private Long id;
    private MemberRole role;
    private String nickName;
    private String email;

    public static LoginUserDTO fromMemberEntity(Member member) {
        return new LoginUserDTO(
                member.getId(),
                member.getRole(),
                member.getNickName(),
                member.getEmail()
        );
    }
//    private Long id;
//    private MemberRole role;
//    private String nickName;
//    private String password;
//    private String email;
//    private LocalDateTime registeredAt;
//    private LocalDateTime deletedAt;
//    private LocalDateTime modifiedAt;
//
//    public static LoginUserDTO fromMemberEntity(Member member) {
//        return new LoginUserDTO(
//                member.getId(),
//                member.getRole(),
//                member.getNickName(),
//                member.getPassword(),
//                member.getEmail(),
//                member.getRegisteredAt(),
//                member.getDeletedAt(),
//                member.getModifiedAt()
//        );
//    }
}
