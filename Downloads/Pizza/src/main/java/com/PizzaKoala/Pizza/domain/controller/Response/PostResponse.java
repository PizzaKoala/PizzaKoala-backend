package com.PizzaKoala.Pizza.domain.controller.Response;

import com.PizzaKoala.Pizza.domain.model.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
@AllArgsConstructor
@Getter
public class PostResponse {
    private Long id;
    private UserResponse member;
    private String title;
    private String desc;
    private Long likes;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime modifiedAt;

    public static PostResponse fromPostDTO(PostDTO post) {
        return new PostResponse(
                post.getId(),
                UserResponse.fromUser(post.getMember()),
                post.getTitle(),
                post.getDesc(),
                post.getLikes(),
                post.getModifiedAt(),
                post.getDeletedAt(),
                post.getCreatedAt()
        );

    }
}
