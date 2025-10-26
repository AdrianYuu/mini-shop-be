package com.adrian.minishop.handler;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class StatelessCookieCsrfTokenRepository implements CsrfTokenRepository {
    private final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    private final String CSRF_PARAM_NAME = "_csrf";

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String token = UUID.randomUUID().toString();
        return new DefaultCsrfToken(CSRF_HEADER_NAME, CSRF_PARAM_NAME, token);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        if (token == null) {
            return;
        }

        Cookie cookie = new Cookie(CSRF_COOKIE_NAME, token.getToken());
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(request.isSecure());
        cookie.setMaxAge(900);
        response.addCookie(cookie);
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        Optional<String> token = Arrays.stream(request.getCookies())
                .filter(c -> CSRF_COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();

        return token.map(t -> new DefaultCsrfToken(CSRF_HEADER_NAME, CSRF_PARAM_NAME, t))
                .orElse(null);
    }
}
