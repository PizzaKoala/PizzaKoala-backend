package com.PizzaKoala.Pizza.domain.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostModifyRequest {
    @Schema(example = "2025년 1월 20일 일기")
    private String title;
    @Schema(description = "내용", example = "오늘은 머리가 아파서 낮에는 많이 누워있었다. 오후에는 촘 나아져서 이것 저것 하며 바삐 하루를 보냈다. 건강이 최오지.")
    private String desc;
//    private List<MultipartFile> file;
}
