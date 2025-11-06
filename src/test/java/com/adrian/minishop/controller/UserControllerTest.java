package com.adrian.minishop.controller;

import com.adrian.minishop.TestHelper;
import com.adrian.minishop.constant.Token;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.entity.User;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {

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
    void shouldGetMeUnauthorized_whenTokenNotExists() throws Exception {
        String csrfToken = testHelper.getCsrfToken();

        mockMvc.perform(
                get("/api/v1/users/me")
                        .header(Token.CSRF_HEADER, csrfToken)
                        .cookie(new Cookie(Token.CSRF_TOKEN, csrfToken))
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
    void shouldGetMeOk_whenTokenExists() throws Exception {
        String csrfToken = testHelper.getCsrfToken();
        String accessToken = testHelper.getAccessToken();

        mockMvc.perform(
                get("/api/v1/users/me")
                        .header(Token.CSRF_HEADER, csrfToken)
                        .cookie(new Cookie(Token.CSRF_TOKEN, csrfToken))
                        .cookie(new Cookie(Token.ACCESS_TOKEN, accessToken))
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
            assertEquals(user.getImageKey(), response.getData().getImageKey());
            assertEquals(user.getRole(), response.getData().getRole());
        });
    }

    @Test
    void shouldPatchMeUnAuthorized_whenTokenNotExists() throws Exception {
        String csrfToken = testHelper.getCsrfToken();

        mockMvc.perform(
                patch("/api/v1/users/me")
                        .header(Token.CSRF_HEADER, csrfToken)
                        .cookie(new Cookie(Token.CSRF_TOKEN, csrfToken))
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
    void shouldPatchMeBadRequest_whenRequestInvalid() throws Exception {
        String csrfToken = testHelper.getCsrfToken();
        String accessToken = testHelper.getAccessToken();

        String name = "";
        String bio = "";
        MockMultipartFile pdf = testHelper.getPdfFile("image");

        mockMvc.perform(
                multipart("/api/v1/users/me")
                        .file(pdf)
                        .param("name", name)
                        .param("bio", bio)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .header(Token.CSRF_HEADER, csrfToken)
                        .cookie(new Cookie(Token.CSRF_TOKEN, csrfToken))
                        .cookie(new Cookie(Token.ACCESS_TOKEN, accessToken))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
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
    void shouldPatchMeOk_whenRequestValid() throws Exception {
        String csrfToken = testHelper.getCsrfToken();
        String accessToken = testHelper.getAccessToken();

        String name = "Budi";
        String bio = "Aku adalah budi";
        MockMultipartFile image = testHelper.getPngFile("image");

        mockMvc.perform(
                multipart("/api/v1/users/me")
                        .file(image)
                        .param("name", name)
                        .param("bio", bio)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        })
                        .header(Token.CSRF_HEADER, csrfToken)
                        .cookie(new Cookie(Token.CSRF_TOKEN, csrfToken))
                        .cookie(new Cookie(Token.ACCESS_TOKEN, accessToken))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });

            assertNull(response.getErrors());

            assertNotNull(response.getData());

            User user = testHelper.getUser();

            assertEquals(name, user.getName());
            assertEquals(bio, user.getBio());
            assertNotNull(user.getImageKey());
        });
    }

}
