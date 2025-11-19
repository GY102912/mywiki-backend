package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import com.jian.community.presentation.validation.EmailFormat;
import com.jian.community.presentation.validation.NicknameFormat;
import com.jian.community.presentation.validation.PasswordFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(

        @Schema(example = "tester@gmail.com")
        @NotBlank(message = ValidationMessage.EMAIL_REQUIRED)
        @EmailFormat
        String email,

        @Schema(example = "password00!!")
        @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
        @PasswordFormat
        String password,

        @Schema(example = "tester")
        @NotBlank(message = ValidationMessage.NICKNAME_REQUIRED)
        @NicknameFormat
        String nickname,

        @Schema(example = "http://localhost:8080//files/images/79432acd-4ce7-4e4d-ab39-84da840218e1.png")
        @NotBlank(message = ValidationMessage.PROFILE_IMAGE_URL_REQUIRED)
        String profileImageUrl
) {
}
