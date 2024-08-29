package com.PizzaKoala.Pizza.domain.controller.response;

import com.PizzaKoala.Pizza.domain.model.SearchMemberNicknameDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SearchNicknamesResponse {
    private Long id;
    private String nickName;
    private String profileImageUrl;


    public static SearchNicknamesResponse fromSearchMemberNicknameSTO(SearchMemberNicknameDTO member) {
        return new SearchNicknamesResponse(
                member.getId(),
                member.getNickName(),
                member.getProfileImageUrl()
        );
    }
}
