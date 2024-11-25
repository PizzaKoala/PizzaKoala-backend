package com.PizzaKoala.Pizza.domain.controller.response;

import com.PizzaKoala.Pizza.domain.dto.PostDTO;
import com.PizzaKoala.Pizza.member.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostResponse {
    private Long id;
    private UserDTO member;
    private String title;
    private String desc;
    private Long likes;
    private String imageProfileUrl;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime modifiedAt;


    public static PostResponse fromPostDTO(PostDTO post) {
        return new PostResponse(
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
