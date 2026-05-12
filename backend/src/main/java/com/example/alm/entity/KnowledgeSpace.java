
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "knowledge_space")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeSpace {
    
    @Id
    @Column(name = "space_id", length = 32)
    private String spaceId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "space_key", length = 50, unique = true)
    private String spaceKey;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
