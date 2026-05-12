
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalTask {
    
    @Id
    @Column(name = "task_id", length = 32)
    private String taskId;
    
    @Column(name = "flow_id", length = 32, nullable = false)
    private String flowId;
    
    @Column(name = "document_id", length = 32)
    private String documentId;
    
    @Column(name = "document_type", length = 50)
    private String documentType;
    
    @Column(name = "current_step", length = 32)
    private String currentStep;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "applicant", length = 32)
    private String applicant;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
