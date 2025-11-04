package com.adrian.minishop.handler;

import com.adrian.minishop.constant.Token;
import com.adrian.minishop.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomCookieCsrfTokenRepository implements CsrfTokenRepository {

    private final CookieUtil cookieUtil;

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        return new DefaultCsrfToken(Token.CSRF_HEADER, Token.CSRF_PARAM, UUID.randomUUID().toString());
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        if (token == null) {
            return;
        }

        cookieUtil.createCookie(response, Token.CSRF_TOKEN, token.getToken(), 900L, false, false, "strict", "/");
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        String token = cookieUtil.getCookieValue(request, Token.CSRF_TOKEN);

        if (token == null) {
            return null;
        }

        return new DefaultCsrfToken(Token.CSRF_HEADER, Token.CSRF_PARAM, token);
    }

}
