package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "design")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Design {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "design_id", length = 32, unique = true, nullable = false)
    private String designId;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "type", length = 20)
    private String type;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "version", length = 20)
    private String version;

    @Column(name = "project_id", length = 32)
    private String projectId;

    @Column(name = "module", length = 100)
    private String module;

    @Column(name = "related_req_id", length = 32)
    private String relatedReqId;

    @Column(name = "designer", length = 50)
    private String designer;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "created_by", length = 32)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}