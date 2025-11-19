package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import com.jian.community.presentation.validation.PasswordFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(

        @Schema(example = "password00!!")
        @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
        String oldPassword,

        @Schema(example = "password00??")
        @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
        @PasswordFormat
        String newPassword
) {
}
