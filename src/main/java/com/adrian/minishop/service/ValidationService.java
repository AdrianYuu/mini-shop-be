package com.adrian.minishop.service;

import com.adrian.minishop.exception.HttpException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;

    public void validate(Object request) {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public boolean isAllFieldNull(Object obj) {
        if (obj == null) {
            return true;
        }

        try {
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);

                Object value = field.get(obj);

                if (Objects.nonNull(value)) {
                    return false;
                }
            }

            return true;
        } catch (IllegalAccessException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to check fields");
        }
    }

}
