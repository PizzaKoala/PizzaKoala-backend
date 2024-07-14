package com.PizzaKoala.Pizza.domain.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@AllArgsConstructor
public class PostCreateRequest {
    private String title;
    private String desc;
    private List<MultipartFile> file;
}
