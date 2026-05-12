
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * PCB设计实体类
 */
@Entity
@Table(name = "hardware_pcb")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HardwarePcb {

    @Id
    @Column(name = "pcb_id", length = 32)
    private String pcbId;

    @Column(name = "project_id", length = 32)
    private String projectId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "file_path", length = 500, nullable = false)
    private String filePath;

    @Column(name = "layers")
    private Integer layers;

    @Column(name = "size", length = 100)
    private String size;

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
