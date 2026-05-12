
package com.example.alm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 3D模型DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Structure3dModelDTO {
    
    private String modelId;
    private String projectId;
    private String name;
    private String filePath;
    private String fileFormat;
    private String version;
    private String status;
    private String thumbnail;
    private String createdBy;
}
