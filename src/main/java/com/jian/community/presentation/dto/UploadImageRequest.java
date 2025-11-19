package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidImage;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadImageRequest(

        @NotNull
        @ValidImage
        MultipartFile image
) {
}
