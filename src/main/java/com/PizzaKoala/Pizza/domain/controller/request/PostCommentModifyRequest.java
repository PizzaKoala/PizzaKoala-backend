package com.PizzaKoala.Pizza.domain.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
@Schema(description = "게시글 댓글 요청(댓글쓰기)")
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class PostCommentModifyRequest {
    @Schema(description = "수정하는 댓글의 아이디",example = "1")
    private Long commentId;
    @Schema(description = "댓글 내용",example = "내가 쓴 나의 게시글이다!!!")
    @NonNull
    private String comment;

}
