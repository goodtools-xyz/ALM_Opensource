
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 2D图纸实体类
 */
@Entity
@Table(name = "structure_2d_drawing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Structure2dDrawing {

    @Id
    @Column(name = "drawing_id", length = 32)
    private String drawingId;

    @Column(name = "project_id", length = 32)
    private String projectId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "file_path", length = 500, nullable = false)
    private String filePath;

    @Column(name = "file_format", length = 50)
    private String fileFormat;

    @Column(name = "version", length = 20)
    private String version = "1.0";

    @Column(name = "status", length = 32)
    private String status = "DRAFT";

    @Column(name = "thumbnail", length = 500)
    private String thumbnail;

    @Column(name = "created_by", length = 32)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
