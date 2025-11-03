package com.adrian.minishop.validation.validator;

import com.adrian.minishop.service.ValidationService;
import com.adrian.minishop.validation.annotation.AtLeastOneFieldNotNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtLeastOneFieldNotNullValidator implements ConstraintValidator<AtLeastOneFieldNotNull, Object> {

    private final ValidationService validationService;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        return !validationService.isAllFieldNull(o);
    }

}
