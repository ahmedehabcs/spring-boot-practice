package com.project.project.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateTaskRequest(

        @NotBlank
        String taskName,

        @NotBlank
        String taskDescription,

        @NotNull
        Long campaignId,

        @NotNull
        Long clientId,

        @Size(max = 5)
        List<String> imagePaths

) {
}