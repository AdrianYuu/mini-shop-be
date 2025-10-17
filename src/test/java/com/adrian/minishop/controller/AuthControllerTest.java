package com.adrian.minishop.controller;

import com.adrian.minishop.TestHelper;
import com.adrian.minishop.dto.request.RegisterRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.enums.Role;
import com.adrian.minishop.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestHelper testHelper;

    @BeforeEach
    void setUp() {
        testHelper.deleteAllUser();
    }

    @Test
    void shouldRegisterCreated_whenRequestIsValid() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        RegisterRequest request = new RegisterRequest();
        request.setName("Adrian Yu");
        request.setEmail("adrian.yu@gmail.com");
        request.setPassword("AdrianYu1!");

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNotNull(response.getData().getId());
            assertEquals(request.getName(), response.getData().getName());
            assertEquals(request.getEmail(), response.getData().getEmail());
            assertNull(response.getData().getBio());
            assertNull(response.getData().getImageUrl());
            assertEquals(Role.USER, response.getData().getRole());
        });
    }

}
