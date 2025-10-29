package com.adrian.minishop.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    public String toSnakeCase(String s) {
        return s.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

}
