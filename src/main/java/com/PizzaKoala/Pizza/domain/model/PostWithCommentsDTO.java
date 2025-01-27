package com.PizzaKoala.Pizza.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 게시글+ 게시글댓글 표준포맷
 */

@Getter
@NoArgsConstructor
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
    private List<CommentDTO> comments;


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
