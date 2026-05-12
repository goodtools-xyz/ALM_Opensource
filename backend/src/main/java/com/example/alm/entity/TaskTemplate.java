
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTemplate {
    
    @Id
    @Column(name = "template_id", length = 32)
    private String templateId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "category", length = 100)
    private String category;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "project_type", length = 50)
    private String projectType;
    
    @Column(name = "config", columnDefinition = "TEXT")
    private String config;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
