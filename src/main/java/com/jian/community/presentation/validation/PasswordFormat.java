package com.jian.community.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$")
public @interface PasswordFormat {
    @OverridesAttribute(constraint = Pattern.class, name = "message")
    String message() default ValidationMessage.INVALID_PASSWORD;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}