package com.project.project.tasks.entity;

import com.project.project.campaign.entity.Campaign;
import com.project.project.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String taskName;

    @Column(nullable = false)
    private String taskDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus taskStatus = TaskStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @Column(nullable = false)
    private String uploadedByName;

    @ElementCollection
    @CollectionTable(
            name = "task_images",
            joinColumns = @JoinColumn(name = "task_id")
    )
    @Column(name = "image_path")
    private List<String> imagePaths  = new ArrayList<>();


}
