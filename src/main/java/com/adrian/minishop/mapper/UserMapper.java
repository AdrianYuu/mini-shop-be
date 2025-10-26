package com.adrian.minishop.mapper;

import com.adrian.minishop.dto.response.UserResponse;
import com.adrian.minishop.entity.User;
import com.adrian.minishop.service.MinioService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    protected MinioService minioService;

    @Mapping(target = "imageUrl", expression = "java(getImageUrl(user.getImageKey()))")
    public abstract UserResponse userToUserResponse(User user);

    protected String getImageUrl(String imageKey) {
        if (imageKey == null || imageKey.isEmpty()) {
            return null;
        }

        return minioService.getFileUrl(imageKey);
    }

}
