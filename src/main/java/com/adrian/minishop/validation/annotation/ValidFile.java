package com.adrian.minishop.validation.annotation;


import com.adrian.minishop.validation.validator.ValidFileValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidFileValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {

    String message() default "File not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    long maxSize() default 1_000_000;

    String[] contentTypes() default {};

    boolean required() default false;

}
