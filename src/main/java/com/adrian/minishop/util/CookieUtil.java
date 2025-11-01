package com.adrian.minishop.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CookieUtil {

    public String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(name))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    public void createCookie(HttpServletResponse response,
                             String name, String value,
                             Long maxAge, Boolean httpOnly,
                             Boolean secure, String sameSite, String path) {

        ResponseCookie cookie = ResponseCookie.from(name, value)
                .maxAge(maxAge)
                .httpOnly(httpOnly)
                .secure(secure)
                .sameSite(sameSite)
                .path(path)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteCookie(HttpServletResponse response,
                             String name, Boolean httpOnly,
                             Boolean secure, String sameSite, String path) {

        ResponseCookie cookie = ResponseCookie.from(name, "")
                .maxAge(0L)
                .httpOnly(httpOnly)
                .secure(secure)
                .sameSite(sameSite)
                .path(path)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}
