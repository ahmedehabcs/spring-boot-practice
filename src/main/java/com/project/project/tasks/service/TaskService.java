package com.project.project.tasks.service;

import com.project.project.campaign.entity.Campaign;
import com.project.project.campaign.repository.CampaignRepository;
import com.project.project.common.exception.ApiException;
import com.project.project.common.images.ImageService;
import com.project.project.tasks.dto.CreateTaskRequest;
import com.project.project.tasks.dto.TaskResponse;
import com.project.project.tasks.dto.UpdateTaskRequest;
import com.project.project.tasks.entity.Task;
import com.project.project.tasks.mapper.TaskMapper;
import com.project.project.tasks.repository.TaskRepository;
import com.project.project.user.entity.Role;
import com.project.project.user.entity.User;
import com.project.project.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;


    public List<TaskResponse> getMyTasks() {
        User user = getCurrentUser();
        if (user.getRole() != Role.CLIENT) throw new ApiException(HttpStatus.FORBIDDEN, "Only clients can use this endpoint");
        return taskRepository.findByClientId(user.getId()).stream().map(TaskMapper::toResponse).toList();
    }

    
    public List<TaskResponse> getTasksByCampaign(Long campaignId) {

        Campaign campaign = campaignRepository.findById(campaignId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Campaign not found"));
        User user = getCurrentUser();

        if (user.getRole() == Role.EMPLOYEE) {
            boolean belongsToCampaign = campaign.getEmployees().stream().anyMatch(employee -> employee.getId().equals(user.getId()));
            if (!belongsToCampaign) throw new ApiException(HttpStatus.FORBIDDEN, "You are not assigned to this campaign");
        }
        return taskRepository.findByCampaignId(campaignId).stream().map(TaskMapper::toResponse).toList();
    }


    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Task not found"));
        User user = getCurrentUser();

        // CLIENT can only see his own task
        if (user.getRole() == Role.CLIENT && !task.getClient().getId().equals(user.getId())) throw new ApiException(HttpStatus.FORBIDDEN, "You cannot view this task");
        
        // EMPLOYEE must belong to the task's campaign
        if (user.getRole() == Role.EMPLOYEE) {
            boolean belongsToCampaign = task.getCampaign().getEmployees().stream().anyMatch(employee -> employee.getId().equals(user.getId()));
            if (!belongsToCampaign) throw new ApiException(HttpStatus.FORBIDDEN, "You cannot view this task");
        }
        return TaskMapper.toResponse(task);
    }


    public TaskResponse createTask(CreateTaskRequest request, List<MultipartFile> images) {

        Campaign campaign = campaignRepository.findById(request.campaignId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Campaign not found"));

        User client = userRepository.findById(request.clientId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Client not found"));

        if (client.getRole() != Role.CLIENT) throw new ApiException(HttpStatus.BAD_REQUEST, client.getName() + " is not a CLIENT");

        // Client must belong to selected campaign
        boolean clientInCampaign = campaign.getClients().stream().anyMatch(c -> c.getId().equals(client.getId()));

        if (!clientInCampaign) throw new ApiException(HttpStatus.BAD_REQUEST, "Client is not linked to this campaign");
        if (images != null && images.size() > 5) throw new ApiException(HttpStatus.BAD_REQUEST, "Maximum 5 images allowed");
        
        User uploader = getCurrentUser();

        Task task = TaskMapper.toEntity(request);

        task.setCampaign(campaign);
        task.setClient(client);
        task.setUploadedByName(uploader.getName());

        if (images != null && !images.isEmpty()) {
            task.setImagePaths(imageService.saveImages(images));
        }
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toResponse(savedTask);
    }


    public TaskResponse updateTask(Long id, UpdateTaskRequest request, List<MultipartFile> images) {

        Task task = taskRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Task not found"));

        if (request.taskName() != null) {
            task.setTaskName(request.taskName());
        }

        if (request.taskDescription() != null) {
            task.setTaskDescription(request.taskDescription());
        }

        if (request.taskStatus() != null) {
            task.setTaskStatus(request.taskStatus());
        }

        // Work out final campaign
        Campaign campaign = task.getCampaign();

        if (request.campaignId() != null) {
            campaign = campaignRepository.findById(request.campaignId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Campaign not found"));
            task.setCampaign(campaign);
        }

        // Work out final client
        User client;

        if (request.clientId() != null) {

            client = userRepository.findById(request.clientId()).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Client not found"));

            if (client.getRole() != Role.CLIENT) {
                throw new ApiException(HttpStatus.BAD_REQUEST, client.getName() + " is not a CLIENT");
            }
        } else {
            client = task.getClient();
        }

        // Always verify final client belongs to final campaign
        boolean clientInCampaign = campaign.getClients().stream().anyMatch(c -> c.getId().equals(client.getId()));

        if (!clientInCampaign) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Client is not linked to this campaign");
        }

        task.setClient(client);

        if (images != null) {
            if (images.size() > 5) throw new ApiException(HttpStatus.BAD_REQUEST, "Maximum 5 images allowed");
            task.setImagePaths(imageService.saveImages(images));
        }

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toResponse(savedTask);
    }


    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Task not found"));
        taskRepository.delete(task);
    }


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }
        return userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
}