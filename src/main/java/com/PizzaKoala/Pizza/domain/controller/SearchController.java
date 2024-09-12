package com.PizzaKoala.Pizza.domain.controller;

import com.PizzaKoala.Pizza.domain.controller.response.PostListResponse;
import com.PizzaKoala.Pizza.domain.controller.response.Response;
import com.PizzaKoala.Pizza.domain.controller.response.SearchNicknamesResponse;
import com.PizzaKoala.Pizza.domain.service.SearchService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/")
@RestController
@AllArgsConstructor
public class SearchController {
    @Autowired
    private final SearchService searchService;
    // 닉네임, 제목, 내용

    /**
     * 최신순- 포스트 검색
     */
    @GetMapping("/search/posts/{keyword}/recent")
    public Response<Page<PostListResponse>> searchPostByRecent(@PathVariable String keyword , Pageable pageable) {
        return Response.success(searchService.SearchRecentPostTitleAndDesc(pageable, keyword).map(PostListResponse::fromPostImageDTO));
    }

    /**
     * 최신포스트순 - 닉네임 검색
     */

    @GetMapping("/search/nicknames/{keyword}/recent")
    public Response<Page<SearchNicknamesResponse>> searchNicknamesByRecent(@PathVariable String keyword, Pageable pageable) {
        return Response.success(searchService.SearchMemberByPosts(pageable,keyword).map(SearchNicknamesResponse::fromSearchMemberNicknameSTO));
    }


    /**
     * 좋아요- 포스트 검색
     */
    @GetMapping("/search/posts/{keyword}/likes")
    public Response<Page<PostListResponse>> searchPostByLikes(@PathVariable String keyword , Pageable pageable) {
        return Response.success(searchService.SearchLikedPostTitleAndDesc(pageable, keyword).map(PostListResponse::fromPostImageDTO));
    }

    /**
     * 팔로워순 - 닉네임
     */

    @GetMapping("/search/nicknames/{keyword}/followers")
    public Response<Page<SearchNicknamesResponse>> searchNicknamesByFollowers(@PathVariable String keyword, Pageable pageable) {
        return Response.success(searchService.SearchNicknameByFollowers(pageable,keyword).map(SearchNicknamesResponse::fromSearchMemberNicknameSTO));
    }


}
