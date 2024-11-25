package com.PizzaKoala.Pizza.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchMemberNicknameDTO {
    private Long id;
    private String nickName;
    private String profileImageUrl;


    public SearchMemberNicknameDTO(Long id, String nickName, String profileImageUrl) {
        this.id=id;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
    }

}
