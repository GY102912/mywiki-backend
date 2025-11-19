package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import com.jian.community.presentation.validation.NicknameFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record NicknameAvailabilityRequest(

        @Schema(example = "tester")
        @NotBlank(message = ValidationMessage.NICKNAME_REQUIRED)
        @NicknameFormat
        String nickname
) {
}
