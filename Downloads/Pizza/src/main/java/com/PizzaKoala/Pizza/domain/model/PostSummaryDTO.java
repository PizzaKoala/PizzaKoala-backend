package com.PizzaKoala.Pizza.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
