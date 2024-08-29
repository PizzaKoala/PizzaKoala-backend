package com.PizzaKoala.Pizza.domain.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostModifyRequest {
    private String title;
    private String desc;
//    private List<MultipartFile> file;
}
