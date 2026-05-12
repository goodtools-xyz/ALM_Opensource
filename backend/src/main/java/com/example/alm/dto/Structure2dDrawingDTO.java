
package com.example.alm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 2D图纸DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Structure2dDrawingDTO {
    
    private String drawingId;
    private String projectId;
    private String name;
    private String filePath;
    private String fileFormat;
    private String version;
    private String status;
    private String thumbnail;
    private String createdBy;
}
