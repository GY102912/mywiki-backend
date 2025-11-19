package com.jian.community.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
@Email(
        regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,15}",
        flags = Pattern.Flag.CASE_INSENSITIVE
)
public @interface EmailFormat {
    @OverridesAttribute(constraint = Email.class, name = "message")
    String message() default ValidationMessage.INVALID_EMAIL;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
