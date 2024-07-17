package com.PizzaKoala.Pizza.domain.controller.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class likeOrUnlikeResponse {
    private char desc_code;

    private String error_desc;
}
