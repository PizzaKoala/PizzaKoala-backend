package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.domain.Repository.CustomMemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.CustomPostRepository;
import com.PizzaKoala.Pizza.domain.Repository.MemberRepository;
import com.PizzaKoala.Pizza.domain.Repository.PostRepository;
import com.PizzaKoala.Pizza.domain.model.PostSummaryDTO;
import com.PizzaKoala.Pizza.domain.model.SearchMemberNicknameDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SearchService {
    private final CustomPostRepository customPostRepository;
    private final CustomMemberRepository customMemberRepository;
    public Page<PostSummaryDTO> SearchRecentPostTitleAndDesc(Pageable pageable, String keyword) {
        return customPostRepository.searchRecentPosts(keyword, pageable);
    }
    public Page<SearchMemberNicknameDTO> SearchMemberByPosts(Pageable pageable, String keyword) {
        return customMemberRepository.searchMemberByRecentPosts(keyword, pageable);

    }
    public Page<PostSummaryDTO> SearchLikedPostTitleAndDesc(Pageable pageable, String keyword) {
        return customPostRepository.searchLikedPosts(keyword, pageable);
    }

    public Page<SearchMemberNicknameDTO> SearchNicknameByFollowers(Pageable pageable, String keyword) {
        return customMemberRepository.searchMemberByMostFollowers(keyword, pageable);

    }
}
