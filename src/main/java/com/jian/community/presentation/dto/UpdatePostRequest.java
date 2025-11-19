package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdatePostRequest(

        @Schema(example = "새로운 게시글 제목")
        @NotBlank(message = ValidationMessage.POST_TITLE_REQUIRED)
        @Size(max = 26, message = ValidationMessage.INVALID_POST_TITLE)
        String title,

        @Schema(example = "새로운 게시글 내용")
        @NotBlank(message = ValidationMessage.POST_CONTENT_REQUIRED)
        String content,

        @Schema(example = "http://localhost:8080//files/images/79432acd-4ce7-4e4d-ab39-84da840218e1.png")
        List<String> postImageUrls
) {
}
