
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_storage")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStorage {
    
    @Id
    @Column(name = "file_id", length = 32)
    private String fileId;
    
    @Column(name = "folder_id", length = 32)
    private String folderId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "path", length = 500)
    private String path;
    
    @Column(name = "iso_file_number", length = 100)
    private String isoFileNumber;
    
    @Column(name = "controlled", length = 1)
    private String controlled;
    
    @Column(name = "status", length = 32)
    private String status;
    
    @Column(name = "version", length = 20)
    private String version;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "file_type", length = 100)
    private String fileType;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "archived_at")
    private LocalDateTime archivedAt;
}
