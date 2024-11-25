package com.PizzaKoala.Pizza.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostSummaryDTO {
    private Long postId;
    private String title;
    private String url;
    private Long imageCount;


    public PostSummaryDTO(Long postId, String title, String imageUrl,Long imageCount) {
        this.postId = postId;
        this.title = title;
        this.url= imageUrl;
        this.imageCount = imageCount;
    }

}
