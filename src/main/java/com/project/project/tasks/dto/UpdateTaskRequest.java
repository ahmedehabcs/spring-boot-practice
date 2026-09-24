package com.project.project.tasks.dto;

import com.project.project.tasks.entity.TaskStatus;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateTaskRequest(

        String taskName,

        String taskDescription,

        TaskStatus taskStatus,

        Long campaignId,

        Long clientId,

        @Size(max = 5)
        List<String> imagePaths

) {
}