
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_audit_trail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileAuditTrail {
    
    @Id
    @Column(name = "audit_id", length = 32)
    private String auditId;
    
    @Column(name = "file_id", length = 32, nullable = false)
    private String fileId;
    
    @Column(name = "action", length = 50)
    private String action;
    
    @Column(name = "user_id", length = 32)
    private String userId;
    
    @Column(name = "user_name", length = 255)
    private String userName;
    
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    
    @Column(name = "details", length = 1000)
    private String details;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
