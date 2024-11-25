package com.PizzaKoala.Pizza.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostWithCommentsDTO {

    private Long memberId;
    private String profileUrl;
    private String nickName;

    private Long id;
    private String title;
    private String desc;
    private Long likes;
    private List<String> url;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Page<CommentDTO> comments;


//    public static PostDTOWithComments fromPostEntity(Post post) {
//        return new PostDTOWithComments(
//                post.getId(),
//                post.getMember().getId(),
//                post.getTitle(),
//                post.getDesc(),
//                post.getLikes(),
//                post.getMember().getProfileImageUrl(),
//                post.getCreatedAt(),
//                post.getDeletedAt(),
//                post.getModifiedAt()
//        );
//    }
}
