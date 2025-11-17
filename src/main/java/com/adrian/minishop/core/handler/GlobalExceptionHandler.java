package com.adrian.minishop.core.handler;

import com.adrian.minishop.dto.response.ErrorResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.core.exception.FileStorageException;
import com.adrian.minishop.core.exception.HttpException;
import com.adrian.minishop.util.StringUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final StringUtil stringUtil;

    @Value("${app.env:dev}")
    private String appEnv;

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
                                        .field(stringUtil.orDefault(err.getKey(), "general"))
                                        .messages(err.getValue())
                                        .build())
                                .toList())
                        .build());
    }

    // Data type mismatch
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<?>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.builder()
                        .errors(e.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(err -> ErrorResponse.builder()
                                        .field(err.getField())
                                        .messages(List.of("Data type mismatch"))
                                        .build())
                                .toList())
                        .build());
    }

    // Throw in Service
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<WebResponse<?>> httpException(HttpException e) {
        List<String> messages = List.of(Objects.requireNonNull(e.getReason()));

        if (appEnv.equals("prod") && e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            messages = List.of("Internal Server Error");
        }

        return ResponseEntity
                .status(e.getStatusCode())
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field(e.getField())
                                        .messages(messages)
                                        .build()
                        ))
                        .build());
    }

    // File Storage Exception
    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<WebResponse<?>> fileStorageException(FileStorageException e) {
        List<String> messages = List.of(Objects.requireNonNull(e.getMessage()));

        if (appEnv.equals("prod") && e.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            messages = List.of("Internal Server Error");
        }

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field("general")
                                        .messages(messages)
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

        List<String> messages = List.of(Objects.requireNonNull(e.getMessage()));

        if (appEnv.equals("prod") && httpStatus == HttpStatus.INTERNAL_SERVER_ERROR) {
            messages = List.of("Internal Server Error");
        }

        return ResponseEntity
                .status(httpStatus)
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field("general")
                                        .messages(messages)
                                        .build()
                        ))
                        .build());
    }

}
