package com.adrian.minishop.util;

import org.springframework.stereotype.Component;

@Component
public class FileUtil {

    public String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }

        return fileName.substring(fileName.lastIndexOf('.'));
    }

}
