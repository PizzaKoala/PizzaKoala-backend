package com.PizzaKoala.Pizza.domain.controller.Response;

import com.PizzaKoala.Pizza.domain.model.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CommentResponse {
    private Long id;
    private String comment;
    private String nickName;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime modifiedAt;

    public static CommentResponse fromCommentDTO(CommentDTO commentDTO) {
        return new CommentResponse(
                commentDTO.getId(),
                commentDTO.getComment(),
                commentDTO.getNickName(),
                commentDTO.getPostId(),
                commentDTO.getModifiedAt(),
                commentDTO.getDeletedAt(),
                commentDTO.getCreatedAt()
        );

    }
}
