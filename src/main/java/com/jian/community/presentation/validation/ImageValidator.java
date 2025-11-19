package com.jian.community.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png");

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ValidationMessage.IMAGE_MUST_NOT_EMPTY)
                    .addConstraintViolation();
            return false;
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ValidationMessage.IMAGE_TOO_LARGE)
                    .addConstraintViolation();
            return false;
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ValidationMessage.INVALID_IMAGE_EXTENSION)
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
