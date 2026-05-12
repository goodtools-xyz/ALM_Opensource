
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "report_instance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportInstance {
    
    @Id
    @Column(name = "instance_id", length = 32)
    private String instanceId;
    
    @Column(name = "template_id", length = 32, nullable = false)
    private String templateId;
    
    @Column(name = "name", length = 255)
    private String name;
    
    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters;
    
    @Column(name = "data", columnDefinition = "TEXT")
    private String data;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "format", length = 20)
    private String format;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}
