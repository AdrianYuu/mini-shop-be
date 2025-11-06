package com.adrian.minishop;

import com.adrian.minishop.constant.Token;
import com.adrian.minishop.dto.request.LoginRequest;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.enums.Role;
import com.adrian.minishop.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Component
@RequiredArgsConstructor
public class TestHelper {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public MockMultipartFile getPngFile(String name) {
        return new MockMultipartFile(
                name,
                "image.png",
                "image/png",
                "image".getBytes()
        );
    }

    public MockMultipartFile getPdfFile(String name) {
        return new MockMultipartFile(
                name,
                "file.pdf",
                "application/pdf",
                "pdf".getBytes()
        );
    }

    public String getCsrfToken() throws Exception {
        var result = mockMvc.perform(
                get("/api/v1/auth/csrf")
        ).andExpectAll(
                status().isNoContent()
        ).andReturn();

        Cookie csrfTokenCookie = result.getResponse().getCookie(Token.CSRF_TOKEN);

        return Objects.nonNull(csrfTokenCookie) ? csrfTokenCookie.getValue() : "";
    }

    public String getAccessToken() throws Exception {
        String csrfToken = getCsrfToken();

        LoginRequest request = LoginRequest.builder()
                .email("adrian@gmail.com")
                .password("Ayu123456!")
                .build();

        var result = mockMvc.perform(
                post("/api/v1/auth/login")
                        .header(Token.CSRF_HEADER, csrfToken)
                        .cookie(new Cookie(Token.CSRF_TOKEN, csrfToken))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andReturn();

        Cookie cookie = result.getResponse().getCookie(Token.ACCESS_TOKEN);

        return Objects.nonNull(cookie) ? cookie.getValue() : null;
    }

    public void deleteAllUser() {
        userRepository.deleteAll();
    }

    public void createUser() {
        User user = User.builder()
                .name("Adrian Yu")
                .email("adrian@gmail.com")
                .password(passwordEncoder.encode("Ayu123456!"))
                .bio(null)
                .imageKey(null)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public User getUser() {
        return userRepository.findFirstByEmail("adrian@gmail.com").orElse(null);
    }

}
