package com.PizzaKoala.Pizza.domain.model;

import com.PizzaKoala.Pizza.domain.entity.Comments;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CommentDTO {
    private Long id;
    private String comment;
    private String nickName;

    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime modifiedAt;

    public static CommentDTO fromCommentEntity(Comments comments) {
        return new CommentDTO(
                comments.getId(),
                comments.getComment(),
                comments.getMember().getNickName(),
                comments.getPostId().getId(),
                comments.getCreatedAt(),
                comments.getDeletedAt(),
                comments.getModifiedAt()
        );
    }

}
