package com.PizzaKoala.Pizza.domain.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "게시글 댓글 요청(댓글쓰기)")
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class PostCommentCreateRequest {
    @Schema(description = "댓글 내용",example = "행복한 하루를 보내셨네용!! 멋져 >_<b ")
    @NonNull
    private String comment;

}
