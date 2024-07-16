package com.PizzaKoala.Pizza.domain.controller.Response;

import com.PizzaKoala.Pizza.domain.model.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CommentResponse {
    private Long memberId;
    private String nickName;
    private String profileUrl;

    private Long id;
    private String comment;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    public static CommentResponse fromCommentDTO(CommentDTO commentDTO) {
        return new CommentResponse(
                commentDTO.getMemberId(),
                commentDTO.getNickName(),
                commentDTO.getProfileUrl(),

                commentDTO.getId(),
                commentDTO.getComment(),
                commentDTO.getPostId(),
                commentDTO.getCreatedAt(),
                commentDTO.getModifiedAt()
        );

    }
}
