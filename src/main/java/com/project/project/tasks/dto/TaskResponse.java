package com.project.project.tasks.dto;

import com.project.project.tasks.entity.TaskStatus;

import java.util.List;

public record TaskResponse(

        Long id,
        String taskName,
        String taskDescription,
        TaskStatus taskStatus,

        Long campaignId,
        String campaignName,

        Long clientId,
        String clientName,

        String uploadedByName,

        List<String> imagePaths

) {
}