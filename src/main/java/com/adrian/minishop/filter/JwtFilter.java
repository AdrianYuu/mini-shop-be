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

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = cookieUtil.getCookie(request, "token");

        boolean tokenValid = jwtUtil.validateToken(token);

        if (tokenValid) {
            String userId = jwtUtil.extractToken(token);

            userRepository.findById(userId).ifPresent(user -> SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
            ));
        }

        filterChain.doFilter(request, response);
    }

}
