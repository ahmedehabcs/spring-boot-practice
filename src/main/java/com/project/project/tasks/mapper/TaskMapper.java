package com.project.project.tasks.mapper;

import com.project.project.tasks.dto.CreateTaskRequest;
import com.project.project.tasks.dto.TaskResponse;
import com.project.project.tasks.entity.Task;

public class TaskMapper {

    public static Task toEntity(CreateTaskRequest request) {
        Task task = new Task();

        task.setTaskName(request.taskName());
        task.setTaskDescription(request.taskDescription());
        task.setImagePaths(request.imagePaths());

        return task;
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTaskName(),
                task.getTaskDescription(),
                task.getTaskStatus(),

                task.getCampaign().getId(),
                task.getCampaign().getCampaignName(),

                task.getClient().getId(),
                task.getClient().getName(),

                task.getUploadedByName(),

                task.getImagePaths()
        );
    }
}