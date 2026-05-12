
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "knowledge_page")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgePage {
    
    @Id
    @Column(name = "page_id", length = 32)
    private String pageId;
    
    @Column(name = "space_id", length = 32, nullable = false)
    private String spaceId;
    
    @Column(name = "parent_page_id", length = 32)
    private String parentPageId;
    
    @Column(name = "title", length = 500, nullable = false)
    private String title;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "content_type", length = 20)
    private String contentType;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "version", length = 20)
    private String version;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_by", length = 32)
    private String updatedBy;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
