
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "approval_step")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalStep {
    
    @Id
    @Column(name = "step_id", length = 32)
    private String stepId;
    
    @Column(name = "flow_id", length = 32, nullable = false)
    private String flowId;
    
    @Column(name = "step_name", length = 255)
    private String stepName;
    
    @Column(name = "step_order")
    private Integer stepOrder;
    
    @Column(name = "approvers", length = 500)
    private String approvers;
    
    @Column(name = "type", length = 32)
    private String type;
    
    @Column(name = "required_approval_count")
    private Integer requiredApprovalCount;
}
