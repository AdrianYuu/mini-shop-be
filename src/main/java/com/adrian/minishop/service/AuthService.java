package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.LoginRequest;
import com.adrian.minishop.dto.request.RegisterRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.enums.Role;
import com.adrian.minishop.exception.HttpException;
import com.adrian.minishop.mapper.UserMapper;
import com.adrian.minishop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBio(null);
        user.setImageKey(null);
        user.setRole(Role.USER);

        user = userRepository.save(user);

        return userMapper.userToUserResponse(user);
    }

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
