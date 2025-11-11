package com.adrian.minishop.util;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {

    public String orDefault(String s, String defaultValue) {
        return (s == null || s.isBlank()) ? defaultValue : s;
    }

}
