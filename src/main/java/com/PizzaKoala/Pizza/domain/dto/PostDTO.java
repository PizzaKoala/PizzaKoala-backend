package com.PizzaKoala.Pizza.domain.dto;

import com.PizzaKoala.Pizza.domain.entity.Post;
import com.PizzaKoala.Pizza.member.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostDTO {
    private Long id;
    private UserDTO member;
    private String title;
    private String desc;
    private Long likes;
    private String imageProfileUrl;

    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime modifiedAt;


    public static PostDTO fromPostEntity(Post post) {
        return new PostDTO(
                post.getId(),
                UserDTO.fromMemberEntity(post.getMember()),
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
