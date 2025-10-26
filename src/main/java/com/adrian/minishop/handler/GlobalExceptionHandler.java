package com.adrian.minishop.handler;

import com.adrian.minishop.dto.response.ErrorResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.exception.FileStorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<WebResponse<?>> responseStatusException(ResponseStatusException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field("general")
                                        .messages(List.of(e.getReason()))
                                        .build()
                        ))
                        .build());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<WebResponse<?>> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field("general")
                                        .messages(List.of("Content-Type is not supported"))
                                        .build()
                        ))
                        .build());
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<WebResponse<?>> handleFileStorageException(FileStorageException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(WebResponse.builder()
                        .errors(List.of(
                                ErrorResponse.builder()
                                        .field("general")
                                        .messages(List.of("File storage error"))
                                        .build()
                        ))
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WebResponse<?>> exception(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
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
