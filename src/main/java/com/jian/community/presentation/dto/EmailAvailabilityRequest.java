package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import com.jian.community.presentation.validation.EmailFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record EmailAvailabilityRequest(

        @Schema(example = "tester@gmail.com")
        @NotBlank(message = ValidationMessage.EMAIL_REQUIRED)
        @EmailFormat
        String email
) {
}
