package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "defect")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Defect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "defect_id", length = 32, unique = true, nullable = false)
    private String defectId;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "severity", length = 20)
    private String severity;

    @Column(name = "priority", length = 20)
    private String priority;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "reproduce_steps", columnDefinition = "TEXT")
    private String reproduceSteps;

    @Column(name = "related_req_id", length = 32)
    private String relatedReqId;

    @Column(name = "related_test_case_id", length = 32)
    private String relatedTestCaseId;

    @Column(name = "project_id", length = 32)
    private String projectId;

    @Column(name = "module", length = 100)
    private String module;

    @Column(name = "reporter", length = 50)
    private String reporter;

    @Column(name = "assignee", length = 50)
    private String assignee;

    @Column(name = "resolved_by", length = 50)
    private String resolvedBy;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_by", length = 32)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}