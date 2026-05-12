
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    @Id
    @Column(name = "emp_id", length = 32)
    private String empId;
    
    @Column(name = "org_id", length = 32)
    private String orgId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "email", length = 255)
    private String email;
    
    @Column(name = "phone", length = 50)
    private String phone;
    
    @Column(name = "job_title", length = 100)
    private String jobTitle;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "sync_source", length = 50)
    private String syncSource;
    
    @Column(name = "external_id", length = 100)
    private String externalId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "synced_at")
    private LocalDateTime syncedAt;
}
