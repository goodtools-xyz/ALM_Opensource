package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    
    @Id
    @Column(name = "project_id", length = 32)
    private String projectId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "code", length = 50, unique = true)
    private String code;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "status", length = 20)
    private String status;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
