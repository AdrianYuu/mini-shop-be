package com.adrian.minishop.controller;

import com.adrian.minishop.constant.Token;
import com.adrian.minishop.dto.request.LoginRequest;
import com.adrian.minishop.dto.request.RegisterRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.service.AuthService;
import com.adrian.minishop.util.CookieUtil;
import com.adrian.minishop.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final CookieUtil cookieUtil;

    private final JwtUtil jwtUtil;

    @GetMapping(
            path = "/csrf"
    )
    public ResponseEntity<WebResponse<Void>> csrf(CsrfToken csrfToken) {
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
    public ResponseEntity<WebResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
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
    public ResponseEntity<WebResponse<UserResponse>> login(@RequestBody LoginRequest request, HttpServletResponse httpServletResponse) {
        UserResponse response = authService.login(request);

        String token = jwtUtil.generateToken(response.getId());
        Long expiration = jwtUtil.getExpiration();

        cookieUtil.createCookie(httpServletResponse, Token.ACCESS_TOKEN, token, expiration / 1000L, true, false, "strict", "/");

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
    public ResponseEntity<WebResponse<Void>> logout(HttpServletResponse httpServletResponse) {
        cookieUtil.deleteCookie(httpServletResponse, Token.ACCESS_TOKEN, true, false, "strict", "/");
        cookieUtil.deleteCookie(httpServletResponse, Token.CSRF_TOKEN, false, false, "strict", "/");

        SecurityContextHolder.clearContext();

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
