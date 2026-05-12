
package com.example.alm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原理图DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HardwareSchematicDTO {
    
    private String schematicId;
    private String projectId;
    private String name;
    private String filePath;
    private String fileType;
    private String version;
    private String status;
    private String thumbnail;
    private String createdBy;
}
