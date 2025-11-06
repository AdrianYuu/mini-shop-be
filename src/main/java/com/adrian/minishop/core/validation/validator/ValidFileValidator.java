package com.adrian.minishop.core.validation.validator;

import com.adrian.minishop.core.validation.annotation.ValidFile;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class ValidFileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private long maxSize;
    private List<String> contentTypes;
    private boolean required;

    @Override
    public void initialize(ValidFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.contentTypes = Arrays.asList(constraintAnnotation.contentTypes());
        this.required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (required) {
            if (file == null || file.isEmpty()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("File is required")
                        .addConstraintViolation();
                return false;
            }
        } else {
            if (file == null || file.isEmpty()) {
                return true;
            }
        }

        if (file.getSize() > maxSize) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File must not exceed " + (maxSize / 1_000_000) + " MB")
                    .addConstraintViolation();
            return false;
        }

        if (!contentTypes.isEmpty() && !contentTypes.contains(file.getContentType())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File must be in these content types: " + String.join(", ", contentTypes))
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
