package com.adrian.minishop.service;

import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.mapper.UserMapper;
import com.adrian.minishop.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;



}
