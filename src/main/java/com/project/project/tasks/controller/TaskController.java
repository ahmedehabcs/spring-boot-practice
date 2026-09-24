package com.project.project.tasks.controller;

import com.project.project.tasks.dto.CreateTaskRequest;
import com.project.project.tasks.dto.TaskResponse;
import com.project.project.tasks.dto.UpdateTaskRequest;
import com.project.project.tasks.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/my")
    public List<TaskResponse> getMyTasks() {
        return taskService.getMyTasks();
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE')")
    @GetMapping("/campaign/{campaignId}")
    public List<TaskResponse> getTasksByCampaign(@PathVariable Long campaignId) {
        return taskService.getTasksByCampaign(campaignId);
    }

    // CLIENT can only see his own task
    // EMPLOYEE must belong to campaign
    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE', 'CLIENT')")
    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TaskResponse createTask(@Valid @RequestPart("task") CreateTaskRequest request, @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return taskService.createTask(request, images);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN', 'EMPLOYEE')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestPart("task") UpdateTaskRequest request, @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        return taskService.updateTask(id, request, images);
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "Task has been deleted";
    }
}