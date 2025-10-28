package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.UpdateUserInformationRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.mapper.UserMapper;
import com.adrian.minishop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final MinioService minioService;

    public UserResponse me(User user) {
        return userMapper.userToUserResponse(user);
    }

    @Transactional
    public UserResponse updateUserInformation(User user, UpdateUserInformationRequest request) {
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

}
