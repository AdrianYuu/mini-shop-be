package com.adrian.minishop;

import com.adrian.minishop.dto.response.CsrfResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.enums.Role;
import com.adrian.minishop.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

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
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isOk()
        ).andReturn();

        WebResponse<CsrfResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        var cookie = result.getResponse().getCookie("XSRF-TOKEN");

        String csrfTokenFromResponseBody = response.getData().getCsrfToken();
        String csrfTokenFromCookie = cookie != null ? cookie.getValue() : null;

        return new String[]{csrfTokenFromResponseBody, csrfTokenFromCookie};
    }

    public void deleteAllUser() {
        userRepository.deleteAll();
    }

    public User createUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("Adrian Yu");
        user.setEmail("adrian@gmail.com");
        user.setPassword(passwordEncoder.encode("Ayu123456!"));
        user.setBio(null);
        user.setImageKey(null);
        user.setRole(Role.USER);

        userRepository.save(user);

        return user;
    }

}
