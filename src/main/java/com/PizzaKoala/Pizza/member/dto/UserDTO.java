package com.PizzaKoala.Pizza.member.dto;

import com.PizzaKoala.Pizza.member.entity.Member;
import com.PizzaKoala.Pizza.member.entity.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private MemberRole role;
    private String nickName;
    private String profileImageUrl;
    private String email;
    private LocalDateTime registeredAt;
    private LocalDateTime deletedAt;
    private LocalDateTime modifiedAt;

    public static UserDTO fromMemberEntity(Member member) {
        return new UserDTO(
                member.getId(),
                member.getRole(),
                member.getNickName(),
                member.getProfileImageUrl(),
                member.getEmail(),
                member.getRegisteredAt(),
                member.getDeletedAt(),
                member.getModifiedAt()
        );
    }
}
