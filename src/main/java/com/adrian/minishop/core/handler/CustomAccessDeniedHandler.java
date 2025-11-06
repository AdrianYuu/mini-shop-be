package com.adrian.minishop.core.handler;

import com.adrian.minishop.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ResponseUtil responseUtil;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        responseUtil.sendErrorResponse(response, HttpStatus.FORBIDDEN, "Forbidden");
    }

}
