package com.adrian.minishop.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class HttpException extends ResponseStatusException {

    private final String field;

    public HttpException(HttpStatusCode status, String reason) {
        super(status, reason);
        this.field = "general";
    }

    public HttpException(HttpStatusCode status, String reason, String field) {
        super(status, reason);
        this.field = field;
    }

}
