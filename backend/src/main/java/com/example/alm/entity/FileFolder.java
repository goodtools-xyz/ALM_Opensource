
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_folder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileFolder {
    
    @Id
    @Column(name = "folder_id", length = 32)
    private String folderId;
    
    @Column(name = "parent_folder_id", length = 32)
    private String parentFolderId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "path", length = 500)
    private String path;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
