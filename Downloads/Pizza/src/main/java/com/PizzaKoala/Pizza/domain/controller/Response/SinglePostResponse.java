package com.PizzaKoala.Pizza.domain.controller.response;

import com.PizzaKoala.Pizza.domain.model.PostDTO;
import com.PizzaKoala.Pizza.domain.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class SinglePostResponse {
    private Long id;
    private UserDTO member;
    private String title;
    private String desc;
    private Long likes;
    private String imageProfileUrl;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime modifiedAt;


    public static SinglePostResponse fromPostDTO(PostDTO post) {
        return new SinglePostResponse(
                post.getId(),
                post.getMember(),
                post.getTitle(),
                post.getDesc(),
                post.getLikes(),
                post.getMember().getProfileImageUrl(),
                post.getCreatedAt(),
                post.getDeletedAt(),
                post.getModifiedAt()
        );
    }
}
