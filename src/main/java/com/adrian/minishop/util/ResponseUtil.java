package com.adrian.minishop.util;

import com.adrian.minishop.dto.response.ErrorResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResponseUtil {

    private final ObjectMapper objectMapper;

    public void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        WebResponse<?> webResponse = WebResponse.builder()
                .errors(List.of(
                        ErrorResponse.builder()
                                .field("general")
                                .messages(List.of(message))
                                .build()
                ))
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(webResponse));
    }

}
