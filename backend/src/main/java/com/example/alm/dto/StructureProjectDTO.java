
package com.example.alm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 结构项目DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StructureProjectDTO {
    
    private String projectId;
    private String name;
    private String description;
    private String status;
    private String createdBy;
}
