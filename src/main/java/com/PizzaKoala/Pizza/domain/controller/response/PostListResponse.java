package com.PizzaKoala.Pizza.domain.controller.response;

import com.PizzaKoala.Pizza.domain.dto.PostSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class PostListResponse {
    private Long id;
    private String title;
    private String imageUrl;
    private Long imageCount;


    public static PostListResponse fromPostImageDTO(PostSummaryDTO post) {
        return new PostListResponse(
                post.getPostId(),
                post.getTitle(),
                post.getUrl(),
                post.getImageCount()
        );
    }
}
