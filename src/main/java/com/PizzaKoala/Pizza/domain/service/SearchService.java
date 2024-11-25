package com.PizzaKoala.Pizza.domain.service;

import com.PizzaKoala.Pizza.member.repository.CustomMemberRepository;
import com.PizzaKoala.Pizza.domain.repository.CustomPostRepository;
import com.PizzaKoala.Pizza.domain.dto.PostSummaryDTO;
import com.PizzaKoala.Pizza.domain.dto.SearchMemberNicknameDTO;
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


    public Page<SearchMemberNicknameDTO> SearchMemberByFollowers(Pageable pageable, String keyword) {
        return customMemberRepository.searchMemberByMostFollowers(keyword, pageable);
    }
}
