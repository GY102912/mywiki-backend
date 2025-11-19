package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(

        @Schema(example = "댓글 내용")
        @NotBlank(message = ValidationMessage.COMMENT_CONTENT_REQUIRED)
        String content
) {
}
