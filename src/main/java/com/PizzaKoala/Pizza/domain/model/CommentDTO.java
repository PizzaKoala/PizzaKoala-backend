package com.PizzaKoala.Pizza.domain.model;

import com.PizzaKoala.Pizza.domain.entity.Comments;
import com.PizzaKoala.Pizza.domain.entity.Member;
import com.PizzaKoala.Pizza.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
//member- id, nickname, profileUrl comment- 시간,커멘트
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CommentDTO {
    private Long memberId;
    private String nickName;
    private String profileUrl;

    private Long id;
    private String comment;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CommentDTO fromCommentEntity(Comments comments) {
        return new CommentDTO(
                comments.getMember().getId(),
                comments.getMember().getNickName(),
                comments.getMember().getProfileImageUrl(),

                comments.getId(),
                comments.getComment(),
                comments.getPostId().getId(),
                comments.getCreatedAt(),
                comments.getModifiedAt()
        );
    }

}
