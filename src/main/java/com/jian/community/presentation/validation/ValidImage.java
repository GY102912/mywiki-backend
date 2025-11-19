package com.jian.community.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
@Documented
public @interface ValidImage {
    String message() default ValidationMessage.INVALID_IMAGE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

