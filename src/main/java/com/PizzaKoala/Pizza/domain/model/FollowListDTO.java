package com.PizzaKoala.Pizza.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FollowListDTO {
    private Long id;
    private String nickName;
    private String profileImageUrl;
    public FollowListDTO(Long id, String nickName, String profileImageUrl) {
        this.id=id;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
    }

}
