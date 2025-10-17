package com.adrian.minishop.mapper;

import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);

}
