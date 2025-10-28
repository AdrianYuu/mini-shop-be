package com.adrian.minishop.handler;

import com.adrian.minishop.dto.response.ErrorResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.exception.FileStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WebResponse<?>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(WebResponse.builder()
                        .errors(e.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .collect(Collectors.groupingBy(FieldError::getField,
                                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())))
                                .entrySet()
                                .stream()
                                .map(err -> ErrorResponse.builder()
                                        .field(err.getKey())
                                        .messages(err.getValue())
                                        .build())
                                .toList())
                        .build());
    }

    // Throw in Service
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<?>> responseStatusException(ResponseStatusException e) {
        String reason = Optional.ofNullable(e.getReason())
                .filter(r -> !r.isBlank())
                .orElse(e.getStatusCode().toString());

        return ResponseEntity
                .status(e.getStatusCode())
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field("general")
                                        .messages(List.of(reason))
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
