
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTemplate {
    
    @Id
    @Column(name = "template_id", length = 32)
    private String templateId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "type", length = 50)
    private String type;
    
    @Column(name = "config", columnDefinition = "TEXT")
    private String config;
    
    @Column(name = "chart_types", length = 200)
    private String chartTypes;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
