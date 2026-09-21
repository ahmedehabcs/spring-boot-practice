package com.project.project.user.dto;

import com.project.project.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 1)
        String name,

        @Email
        String email,

        Role role,
        Boolean active
) {
}
