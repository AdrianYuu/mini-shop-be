package com.adrian.minishop.core.validation.annotation;

import com.adrian.minishop.core.validation.validator.AtLeastOneFieldNotNullValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtLeastOneFieldNotNullValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneFieldNotNull {

    String message() default "At least one field must be present";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
