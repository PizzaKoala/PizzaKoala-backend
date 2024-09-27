package com.PizzaKoala.Pizza.domain.controller.response;

import com.PizzaKoala.Pizza.domain.model.FollowListDTO;
import com.PizzaKoala.Pizza.domain.model.SearchMemberNicknameDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FollowListResponse {
    private Long id;
    private String nickName;
    private String profileImageUrl;


    public static FollowListResponse fromFollowListDTO(FollowListDTO member) {
        return new FollowListResponse(
                member.getId(),
                member.getNickName(),
                member.getProfileImageUrl()
        );
    }
}
