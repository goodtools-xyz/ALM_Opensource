
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "role")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    @Id
    @Column(name = "role_id", length = 32)
    private String roleId;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "code", length = 50, unique = true)
    private String code;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "permissions", columnDefinition = "TEXT")
    private String permissions;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
