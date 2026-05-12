
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_flow")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalFlow {
    
    @Id
    @Column(name = "flow_id", length = 32)
    private String flowId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "type", length = 50)
    private String type;
    
    @Column(name = "config", columnDefinition = "TEXT")
    private String config;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
