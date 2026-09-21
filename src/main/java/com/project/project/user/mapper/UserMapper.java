package com.project.project.user.mapper;

import com.project.project.user.dto.CreateUserRequest;
import com.project.project.user.dto.UserResponse;
import com.project.project.user.entity.User;

public class UserMapper {

    public static User toEntity(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setName(createUserRequest.name());
        user.setEmail(createUserRequest.email());
        user.setPassword(createUserRequest.password());
        user.setRole(createUserRequest.role());
        return user;
    }

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }
}
