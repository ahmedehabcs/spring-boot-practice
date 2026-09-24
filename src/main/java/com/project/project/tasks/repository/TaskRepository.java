package com.project.project.tasks.repository;

import com.project.project.tasks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCampaignId(Long campaignId);
    List<Task> findByClientId(Long clientId);
}
