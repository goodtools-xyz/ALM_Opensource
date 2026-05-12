
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "kanban_board")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KanbanBoard {
    
    @Id
    @Column(name = "board_id", length = 32)
    private String boardId;
    
    @Column(name = "name", length = 255, nullable = false)
    private String name;
    
    @Column(name = "description", length = 1000)
    private String description;
    
    @Column(name = "columns", columnDefinition = "TEXT")
    private String columns;
    
    @Column(name = "card_fields", columnDefinition = "TEXT")
    private String cardFields;
    
    @Column(name = "filter_config", columnDefinition = "TEXT")
    private String filterConfig;
    
    @Column(name = "project_id", length = 32)
    private String projectId;
    
    @Column(name = "shared", length = 1)
    private String shared;
    
    @Column(name = "created_by", length = 32)
    private String createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
