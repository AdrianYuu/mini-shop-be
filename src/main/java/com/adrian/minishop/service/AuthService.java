package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.LoginRequest;
import com.adrian.minishop.dto.request.RegisterRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.entity.Role;
import com.adrian.minishop.core.exception.HttpException;
import com.adrian.minishop.dto.mapper.UserMapper;
import com.adrian.minishop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final ValidationService validationService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        validationService.validate(request);

        boolean emailExists = userRepository.existsByEmail(request.getEmail());

        if (emailExists) {
            throw new HttpException(HttpStatus.CONFLICT, "Email already exists", "email");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .bio(null)
                .imageKey(null)
                .role(Role.USER)
                .build();

        user = userRepository.save(user);

        return userMapper.userToUserResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse login(LoginRequest request) {
        validationService.validate(request);

        User user = userRepository.findFirstByEmail(request.getEmail())
                .orElseThrow(() -> new HttpException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        boolean passwordValid = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordValid) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return userMapper.userToUserResponse(user);
    }

}
