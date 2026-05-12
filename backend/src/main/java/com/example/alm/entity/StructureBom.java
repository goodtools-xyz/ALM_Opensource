
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 结构BOM实体类
 */
@Entity
@Table(name = "structure_bom")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StructureBom {

    @Id
    @Column(name = "bom_id", length = 32)
    private String bomId;

    @Column(name = "project_id", length = 32)
    private String projectId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "version", length = 20)
    private String version = "1.0";

    @Column(name = "items", columnDefinition = "JSON")
    private String items;

    @Column(name = "status", length = 32)
    private String status = "ACTIVE";

    @Column(name = "created_by", length = 32)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
