package com.PizzaKoala.Pizza.domain.Repository;

import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.model.PostSummaryDTO;
import com.PizzaKoala.Pizza.domain.model.SearchMemberNicknameDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMemberRepository {
    public Page<SearchMemberNicknameDTO> searchMemberByRecentPosts(String keyword, Pageable pageable);
    public Page<SearchMemberNicknameDTO> searchMemberByMostFollowers(String keyword, Pageable pageable);


}
