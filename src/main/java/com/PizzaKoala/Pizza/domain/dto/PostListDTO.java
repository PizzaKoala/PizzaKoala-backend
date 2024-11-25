package com.PizzaKoala.Pizza.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostListDTO {
    private Long id;
    private String title;
    private String url;
    private Long imageCount;


    public PostListDTO(Long postId, String title, String imageUrl,Long imageCount) {
        this.id = postId;
        this.title = title;
        this.url= imageUrl;
        this.imageCount = imageCount;
    }


}
