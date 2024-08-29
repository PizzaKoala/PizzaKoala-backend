package com.PizzaKoala.Pizza.domain.controller.request;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class PostCommentRequest {
    private String comment;
}
