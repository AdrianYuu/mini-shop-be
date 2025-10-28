package com.adrian.minishop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class FileStorageException extends RuntimeException {

    private final HttpStatus httpStatus;

    public FileStorageException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
