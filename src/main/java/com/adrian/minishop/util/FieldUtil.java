package com.adrian.minishop.util;

import com.adrian.minishop.core.exception.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Component
public class FieldUtil {

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
