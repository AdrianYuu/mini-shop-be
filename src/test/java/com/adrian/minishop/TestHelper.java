package com.adrian.minishop;

import com.adrian.minishop.dto.request.LoginRequest;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.enums.Role;
import com.adrian.minishop.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Component
@RequiredArgsConstructor
public class TestHelper {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public String[] getCsrfToken() throws Exception {
        var result = mockMvc.perform(
                get("/api/v1/auth/csrf")
        ).andExpectAll(
                status().isNoContent()
        ).andReturn();

        Cookie csrfTokenCookie = result.getResponse().getCookie("csrf-token");
        Cookie springCsrfTokenCookie = result.getResponse().getCookie("XSRF-TOKEN");

        return new String[]{
                csrfTokenCookie != null ? csrfTokenCookie.getValue() : "",
                springCsrfTokenCookie != null ? springCsrfTokenCookie.getValue() : ""
        };
    }

    public String getToken() throws Exception {
        String[] csrfToken = getCsrfToken();

        LoginRequest request = LoginRequest.builder()
                .email("adrian@gmail.com")
                .password("Ayu123456!")
                .build();

        var result = mockMvc.perform(
                post("/api/v1/auth/login")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andReturn();

        Cookie cookie = result.getResponse().getCookie("token");

        return cookie != null ? cookie.getValue() : null;
    }

    public void deleteAllUser() {
        userRepository.deleteAll();
    }

    public void createUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("Adrian Yu");
        user.setEmail("adrian@gmail.com");
        user.setPassword(passwordEncoder.encode("Ayu123456!"));
        user.setBio(null);
        user.setImageKey(null);
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public User getUser() {
        return userRepository.findFirstByEmail("adrian@gmail.com").orElse(null);
    }

}
