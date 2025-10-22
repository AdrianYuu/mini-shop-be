package com.adrian.minishop.controller;

import com.adrian.minishop.TestHelper;
import com.adrian.minishop.dto.request.LoginRequest;
import com.adrian.minishop.dto.request.RegisterRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.enums.Role;
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

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        testHelper.createUser();
    }

    @Test
        void shouldCsrfOk_whenRequestIsValid() throws Exception {
        mockMvc.perform(
                get("/api/v1/auth/csrf")
        ).andExpectAll(
                status().isNoContent()
        ).andDo(result -> {
            Collection<String> setCookies = result.getResponse().getHeaders("Set-Cookie");
            assertFalse(setCookies.isEmpty());
            assertTrue(setCookies.stream().anyMatch(c -> c.startsWith("csrf-token=")));
            assertTrue(setCookies.stream().anyMatch(c -> c.startsWith("XSRF-TOKEN=")));
        });
    }

    @Test
    void shouldRegisterBadRequest_whenRequestInvalid() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        RegisterRequest request = RegisterRequest.builder()
                .name("")
                .email("")
                .password("")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getData());

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void shouldRegisterBadRequest_whenEmailExists() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        RegisterRequest request = RegisterRequest.builder()
                .name("Adrian Yu")
                .email("adrian@gmail.com")
                .password("Ayu123456!")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getData());

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void shouldRegisterCreated_whenRequestValid() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        RegisterRequest request = RegisterRequest.builder()
                .name("Adrian Yu")
                .email("adrian.yu@gmail.com")
                .password("Ayu123456!")
                .build();

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

            assertNull(response.getErrors());

            assertNotNull(response.getData().getId());
            assertEquals(request.getName(), response.getData().getName());
            assertEquals(request.getEmail(), response.getData().getEmail());
            assertNull(response.getData().getBio());
            assertNull(response.getData().getImageUrl());
            assertEquals(Role.USER, response.getData().getRole());
        });
    }

    @Test
    void shouldLoginBadRequest_whenRequestInvalid() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        LoginRequest request = LoginRequest.builder()
                .email("")
                .password("")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getData());

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void shouldLoginUnauthorized_whenEmailNotExists() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        LoginRequest request = LoginRequest.builder()
                .email("adrian123@gmail.com")
                .password("Ayu123456!")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getData());

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void shouldLoginUnauthorized_whenPasswordWrong() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        LoginRequest request = LoginRequest.builder()
                .email("adrian@gmail.com")
                .password("Ayu123456789!")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getData());

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void shouldLoginOk_whenRequestValid() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        LoginRequest request = LoginRequest.builder()
                .email("adrian@gmail.com")
                .password("Ayu123456!")
                .build();

        mockMvc.perform(
                post("/api/v1/auth/login")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());

            assertEquals(request.getEmail(), response.getData().getEmail());

            String setCookie = result.getResponse().getHeader("Set-Cookie");
            assertNotNull(setCookie);
            assertTrue(setCookie.contains("token="));
        });
    }

    @Test
    void shouldMeUnauthorized_whenTokenNotExists() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        mockMvc.perform(
                get("/api/v1/auth/me")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getData());

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void shouldMeOk_whenTokenExists() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();
        String token = testHelper.getToken();

        mockMvc.perform(
                get("/api/v1/auth/me")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .cookie(new Cookie("token", token))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());

            User user = testHelper.getUser();
            assertEquals(user.getId(), response.getData().getId());
            assertEquals(user.getName(), response.getData().getName());
            assertEquals(user.getEmail(), response.getData().getEmail());
            assertEquals(user.getBio(), response.getData().getBio());
            assertEquals(user.getRole(), response.getData().getRole());
        });
    }

    @Test
    void shouldLogoutUnauthorized_whenTokenNotExists() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();

        mockMvc.perform(
                post("/api/v1/auth/logout")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<?> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getData());

            assertNotNull(response.getErrors());
        });
    }

    @Test
    void shouldLogoutOk_whenTokenExists() throws Exception {
        String[] csrfToken = testHelper.getCsrfToken();
        String token = testHelper.getToken();

        mockMvc.perform(
                post("/api/v1/auth/logout")
                        .header("X-XSRF-TOKEN", csrfToken[0])
                        .cookie(new Cookie("XSRF-TOKEN", csrfToken[1]))
                        .cookie(new Cookie("token", token))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpectAll(
                status().isNoContent()
        ).andDo(result -> {
            Collection<String> setCookies = result.getResponse().getHeaders("Set-Cookie");
            assertFalse(setCookies.isEmpty());
            assertTrue(setCookies.stream().anyMatch(c -> c.startsWith("token=;")));
            assertTrue(setCookies.stream().anyMatch(c -> c.startsWith("XSRF-TOKEN=;")));
        });
    }

}
