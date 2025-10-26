package com.adrian.minishop.controller;

import com.adrian.minishop.dto.request.RegisterRequest;
import com.adrian.minishop.dto.request.UpdateUserInformationRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.dto.response.WebResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(
            path = "/me",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<UserResponse>> me(@AuthenticationPrincipal User user) {
        UserResponse response = userService.me(user);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<UserResponse>builder()
                        .data(response)
                        .build());
    }

    @PatchMapping(
            path = "/me",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<UserResponse>> updateUserInformation(
            @AuthenticationPrincipal User user,
            @Valid @ModelAttribute UpdateUserInformationRequest request
    ) {
        UserResponse response = userService.updateUserInformation(user, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(WebResponse.<UserResponse>builder()
                        .data(response)
                        .build());
    }

}
