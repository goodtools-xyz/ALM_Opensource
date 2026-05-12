
package com.example.alm.service;

import com.example.alm.dto.*;
import com.example.alm.entity.*;

import java.util.List;

/**
 * 结构工程师服务接口
 */
public interface StructureService {

    // 结构项目管理
    StructureProject createProject(StructureProjectDTO dto);
    StructureProject getProjectById(String projectId);
    List<StructureProject> getAllProjects();
    StructureProject updateProject(String projectId, StructureProjectDTO dto);
    void deleteProject(String projectId);

    // 3D模型管理
    Structure3dModel create3dModel(Structure3dModelDTO dto);
    Structure3dModel get3dModelById(String modelId);
    List<Structure3dModel> get3dModelsByProject(String projectId);
    Structure3dModel update3dModel(String modelId, Structure3dModelDTO dto);
    void delete3dModel(String modelId);

    // 2D图纸管理
    Structure2dDrawing create2dDrawing(Structure2dDrawingDTO dto);
    Structure2dDrawing get2dDrawingById(String drawingId);
    List<Structure2dDrawing> get2dDrawingsByProject(String projectId);
    Structure2dDrawing update2dDrawing(String drawingId, Structure2dDrawingDTO dto);
    void delete2dDrawing(String drawingId);
}
