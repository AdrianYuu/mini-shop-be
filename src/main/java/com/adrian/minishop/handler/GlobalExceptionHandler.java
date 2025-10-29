package com.adrian.minishop.handler;

import com.adrian.minishop.dto.response.ErrorResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.exception.FileStorageException;
import com.adrian.minishop.exception.HttpException;
import com.adrian.minishop.util.StringUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final StringUtil stringUtil;

    // Validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WebResponse<?>> constraintViolationException(ConstraintViolationException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.builder()
                        .errors(e.getConstraintViolations()
                                .stream()
                                .collect(Collectors.groupingBy(
                                        violation -> violation.getPropertyPath().toString(),
                                        Collectors.mapping(ConstraintViolation::getMessage, Collectors.toList())
                                ))
                                .entrySet()
                                .stream()
                                .map(err -> ErrorResponse.builder()
                                        .field(stringUtil.toSnakeCase(err.getKey()))
                                        .messages(err.getValue())
                                        .build())
                                .toList())
                        .build());
    }

    // Throw in Service
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<WebResponse<?>> httpException(HttpException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field(stringUtil.toSnakeCase(e.getField()))
                                        .messages(List.of(Objects.requireNonNull(e.getReason())))
                                        .build()
                        ))
                        .build());
    }

    // File Storage Exception
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<WebResponse<?>> fileStorageException(FileStorageException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field("general")
                                        .messages(List.of(e.getMessage()))
                                        .build()
                        ))
                        .build());
    }

    // Other Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<?>> exception(Exception e) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        if (e instanceof org.springframework.web.ErrorResponse errorResponse) {
            httpStatus = HttpStatus.valueOf(errorResponse.getStatusCode().value());
        }

        return ResponseEntity
                .status(httpStatus)
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field("general")
                                        .messages(List.of(e.getMessage()))
                                        .build()
                        ))
                        .build());
    }

}
