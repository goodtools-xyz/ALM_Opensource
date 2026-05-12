
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "knowledge_page_version")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgePageVersion {
    
    @Id
    @Column(name = "version_id", length = 32)
    private String versionId;
    
    @Column(name = "page_id", length = 32, nullable = false)
    private String pageId;
    
    @Column(name = "version", length = 20, nullable = false)
    private String version;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "change_log", length = 500)
    private String changeLog;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
