
package com.example.alm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PCB设计DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HardwarePcbDTO {
    
    private String pcbId;
    private String projectId;
    private String name;
    private String filePath;
    private Integer layers;
    private String size;
    private String version;
    private String status;
    private String thumbnail;
    private String createdBy;
}
