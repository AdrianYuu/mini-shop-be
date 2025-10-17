package com.adrian.minishop.service;

import com.adrian.minishop.dto.request.LoginRequest;
import com.adrian.minishop.dto.request.RegisterRequest;
import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.enums.Role;
import com.adrian.minishop.mapper.UserMapper;
import com.adrian.minishop.repository.UserRepository;
import com.adrian.minishop.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponse register(RegisterRequest request) {
        boolean emailExists = userRepository.existsByEmail(request.getEmail());

        if (emailExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setBio(null);
        user.setImageKey(null);
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return userToUserResponse(savedUser);
    }

    public UserResponse login(LoginRequest request) {
        User user = userRepository.findFirstByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        boolean passwordValid = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return userToUserResponse(user);
    }

    public UserResponse userToUserResponse(User user) {
        return userMapper.userToUserResponse(user);
    }

    public String generateToken(String userId) {
        return jwtUtil.generateToken(userId);
    }

    public Long getExpiration() {
        return jwtUtil.getExpiration();
    }

}
