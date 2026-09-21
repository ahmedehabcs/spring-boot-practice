package com.project.project.user.dto;

import com.project.project.user.entity.Role;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role,
        Boolean active
) {
}
