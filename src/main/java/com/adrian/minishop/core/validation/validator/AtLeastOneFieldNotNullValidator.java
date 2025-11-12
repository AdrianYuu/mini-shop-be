package com.adrian.minishop.core.validation.validator;

import com.adrian.minishop.core.validation.annotation.AtLeastOneFieldNotNull;
import com.adrian.minishop.util.FieldUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtLeastOneFieldNotNullValidator implements ConstraintValidator<AtLeastOneFieldNotNull, Object> {

    private final FieldUtil fieldUtil;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        return !fieldUtil.isAllFieldNull(o);
    }

}
