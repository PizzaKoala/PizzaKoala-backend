package com.PizzaKoala.Pizza.member.repository;

import com.PizzaKoala.Pizza.domain.dto.SearchMemberNicknameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMemberRepository {
    public Page<SearchMemberNicknameDTO> searchMemberByRecentPosts(String keyword, Pageable pageable);
    public Page<SearchMemberNicknameDTO> searchMemberByMostFollowers(String keyword, Pageable pageable);


}
