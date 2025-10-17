package com.adrian.minishop.filter;

import com.adrian.minishop.entity.User;
import com.adrian.minishop.repository.UserRepository;
import com.adrian.minishop.util.CookieUtil;
import com.adrian.minishop.util.JwtUtil;
import com.adrian.minishop.util.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final CookieUtil cookieUtil;

    private final ResponseUtil responseUtil;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = cookieUtil.getCookie(request, "token");

            boolean tokenValid = jwtUtil.validateToken(token);

            if (token == null || !tokenValid) {
                responseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized");
                return;
            }

            String userId = jwtUtil.extractToken(token);

            User user = userRepository.findById(userId)
                    .orElse(null);

            if (user == null) {
                responseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized");
                return;
            }

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            );

            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            responseUtil.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/login") ||
                path.startsWith("/api/v1/auth/register") ||
                path.startsWith("/api/v1/auth/csrf");
    }

}
