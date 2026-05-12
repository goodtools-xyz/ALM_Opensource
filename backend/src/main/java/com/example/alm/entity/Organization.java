
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "organization")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    
    @Id
    @Column(name = "org_id", length = 32)
    private String orgId;
    
    @Column(name = "parent_org_id", length = 32)
    private String parentOrgId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "code", length = 100)
    private String code;
    
    @Column(name = "type", length = 50)
    private String type;
    
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
