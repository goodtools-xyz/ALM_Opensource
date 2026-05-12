
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 结构项目实体类
 */
@Entity
@Table(name = "structure_project")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StructureProject {

    @Id
    @Column(name = "project_id", length = 32)
    private String projectId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status", length = 32)
    private String status = "PLANNING";

    @Column(name = "created_by", length = 32)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
