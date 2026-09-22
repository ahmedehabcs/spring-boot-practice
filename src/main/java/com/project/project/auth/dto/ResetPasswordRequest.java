package com.project.project.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @Size(min = 8)
        @NotBlank
        String password,

        @NotBlank
        String token
) {
}
