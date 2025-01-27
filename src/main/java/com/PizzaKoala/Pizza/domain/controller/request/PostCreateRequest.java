package com.PizzaKoala.Pizza.domain.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.annotations.NotNull;

import java.util.List;
@Schema(description = "게시글 쓰기")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateRequest {
    @Schema(description = "제목", example = "2025년 1월 20일 일기")
    @NotNull
    private String title;
    @Schema(description = "내용", example = "오늘은 머리가 아파서 낮에는 많이 누워있었다. 오후에는 촘 나아져서 이것 저것 하며 바삐 하루를 보냈다. 건강이 최오지.")
    private String desc;
}
