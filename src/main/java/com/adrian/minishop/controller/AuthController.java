package com.adrian.minishop.controller;

import com.adrian.minishop.dto.request.LoginRequest;
import com.adrian.minishop.dto.request.RegisterRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.service.AuthService;
import com.adrian.minishop.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final CookieUtil cookieUtil;

    @GetMapping(
            path = "/csrf"
    )
    public ResponseEntity<WebResponse<?>> csrf(CsrfToken csrfToken) {
        csrfToken.getToken();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping(
            path = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(WebResponse.<UserResponse>builder()
                        .data(response)
                        .build());
    }

    @PostMapping(
            path = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<UserResponse>> login(@Valid @RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {
        UserResponse response = authService.login(request);

        String token = authService.generateToken(response.getId());
        Long expiration = authService.getExpiration();

        Cookie tokenCookie = cookieUtil.createCookie("token",
                token,
                (int) (expiration / 1000),
                true,
                false,
                "/");

        httpServletResponse.addCookie(tokenCookie);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<UserResponse>builder()
                        .data(response)
                        .build());
    }

    @GetMapping(
            path = "/me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<UserResponse>> me(@AuthenticationPrincipal User user) {
        UserResponse response = authService.me(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<UserResponse>builder()
                        .data(response)
                        .build());
    }

    @PostMapping(
            path = "/logout",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<?>> logout(HttpServletResponse httpServletResponse) {
        Cookie tokenCookie = cookieUtil.createCookie("token",
                null,
                0,
                true,
                false,
                "/");

        Cookie csrfTokenCookie = cookieUtil.createCookie("csrf-token",
                null,
                0,
                true,
                false,
                "/");

        Cookie springCsrfTokenCookie = cookieUtil.createCookie("XSRF-TOKEN",
                null,
                0,
                true,
                false,
                "/");

        httpServletResponse.addCookie(tokenCookie);
        httpServletResponse.addCookie(csrfTokenCookie);
        httpServletResponse.addCookie(springCsrfTokenCookie);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
