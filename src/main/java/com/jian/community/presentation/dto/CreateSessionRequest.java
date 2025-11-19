package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import com.jian.community.presentation.validation.EmailFormat;
import com.jian.community.presentation.validation.PasswordFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateSessionRequest(

        @Schema(example = "tester@gmail.com")
        @NotBlank(message = ValidationMessage.EMAIL_REQUIRED)
        @EmailFormat
        String email,

        @Schema(example = "password00!!")
        @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
        @PasswordFormat
        String password
) {
}
