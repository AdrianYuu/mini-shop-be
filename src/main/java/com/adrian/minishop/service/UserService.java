package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.UpdateUserInformationRequest;
import com.adrian.minishop.dto.request.UpdateUserPasswordRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.exception.HttpException;
import com.adrian.minishop.mapper.UserMapper;
import com.adrian.minishop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ValidationService validationService;

    private final MinioService minioService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserResponse me(User user) {
        return userMapper.userToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUserInformation(User user, UpdateUserInformationRequest request) {
        validationService.validate(request);

        if (Objects.nonNull(request.getName())) {
            user.setName(request.getName());
        }

        if (Objects.nonNull(request.getBio())) {
            user.setBio(request.getBio());
        }
        if (Objects.nonNull(request.getImage())) {
            if (Objects.nonNull(user.getImageKey())) {
                minioService.removeFile(user.getImageKey());
            }

            String key = minioService.uploadFile(request.getImage(), minioService.getUserBucket());
            user.setImageKey(key);
        }

        userRepository.save(user);

        return userMapper.userToUserResponse(user);
    }

    @Transactional
    public void updateUserPassword(User user, UpdateUserPasswordRequest request) {
        validationService.validate(request);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Invalid password", "password");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new HttpException(HttpStatus.BAD_REQUEST, "New password can't be the same as the old password", "newPassword");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

}
