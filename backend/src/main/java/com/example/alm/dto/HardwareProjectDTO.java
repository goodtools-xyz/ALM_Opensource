
package com.example.alm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 硬件项目DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HardwareProjectDTO {
    
    private String projectId;
    private String name;
    private String description;
    private String status;
    private String createdBy;
}
