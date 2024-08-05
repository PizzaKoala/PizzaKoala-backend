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
    @GetMapping("/search/{keyword}/posts")
    public Response<Page<PostListResponse>> searchKeywordInPosts(@PathVariable String keyword ,Pageable pageable) {
        return Response.success(searchService.SearchPostTitleAndDesc(pageable, keyword).map(PostListResponse::fromPostImageDTO));
    }
    @GetMapping("/search/{keyword}/nicknames")
    public Response<Page<SearchNicknamesResponse>> searchKeywordInMemberNicknames(@PathVariable String keyword, Pageable pageable) {
        return Response.success(searchService.SearchMemberNickname(pageable,keyword).map(SearchNicknamesResponse::fromSearchMemberNicknameSTO));
    }
}
