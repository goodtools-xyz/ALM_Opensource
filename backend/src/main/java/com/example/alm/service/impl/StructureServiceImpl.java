
package com.example.alm.service.impl;

import com.example.alm.dto.*;
import com.example.alm.entity.*;
import com.example.alm.repository.*;
import com.example.alm.service.StructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 结构工程师服务实现
 */
@Service
public class StructureServiceImpl implements StructureService {

    @Autowired
    private StructureProjectRepository projectRepository;

    @Autowired
    private Structure3dModelRepository modelRepository;

    @Autowired
    private Structure2dDrawingRepository drawingRepository;

    @Override
    public StructureProject createProject(StructureProjectDTO dto) {
        StructureProject project = new StructureProject();
        project.setProjectId(UUID.randomUUID().toString().replace("-", ""));
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStatus(dto.getStatus() != null ? dto.getStatus() : "PLANNING");
        project.setCreatedBy(dto.getCreatedBy());
        return projectRepository.save(project);
    }

    @Override
    public StructureProject getProjectById(String projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    @Override
    public List<StructureProject> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public StructureProject updateProject(String projectId, StructureProjectDTO dto) {
        StructureProject project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            if (dto.getName() != null) project.setName(dto.getName());
            if (dto.getDescription() != null) project.setDescription(dto.getDescription());
            if (dto.getStatus() != null) project.setStatus(dto.getStatus());
            return projectRepository.save(project);
        }
        return null;
    }

    @Override
    public void deleteProject(String projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public Structure3dModel create3dModel(Structure3dModelDTO dto) {
        Structure3dModel model = new Structure3dModel();
        model.setModelId(UUID.randomUUID().toString().replace("-", ""));
        model.setProjectId(dto.getProjectId());
        model.setName(dto.getName());
        model.setFilePath(dto.getFilePath());
        model.setFileFormat(dto.getFileFormat());
        model.setVersion(dto.getVersion() != null ? dto.getVersion() : "1.0");
        model.setStatus(dto.getStatus() != null ? dto.getStatus() : "DRAFT");
        model.setThumbnail(dto.getThumbnail());
        model.setCreatedBy(dto.getCreatedBy());
        return modelRepository.save(model);
    }

    @Override
    public Structure3dModel get3dModelById(String modelId) {
        return modelRepository.findById(modelId).orElse(null);
    }

    @Override
    public List<Structure3dModel> get3dModelsByProject(String projectId) {
        return modelRepository.findByProjectId(projectId);
    }

    @Override
    public Structure3dModel update3dModel(String modelId, Structure3dModelDTO dto) {
        Structure3dModel model = modelRepository.findById(modelId).orElse(null);
        if (model != null) {
            if (dto.getName() != null) model.setName(dto.getName());
            if (dto.getFilePath() != null) model.setFilePath(dto.getFilePath());
            if (dto.getFileFormat() != null) model.setFileFormat(dto.getFileFormat());
            if (dto.getVersion() != null) model.setVersion(dto.getVersion());
            if (dto.getStatus() != null) model.setStatus(dto.getStatus());
            if (dto.getThumbnail() != null) model.setThumbnail(dto.getThumbnail());
            return modelRepository.save(model);
        }
        return null;
    }

    @Override
    public void delete3dModel(String modelId) {
        modelRepository.deleteById(modelId);
    }

    @Override
    public Structure2dDrawing create2dDrawing(Structure2dDrawingDTO dto) {
        Structure2dDrawing drawing = new Structure2dDrawing();
        drawing.setDrawingId(UUID.randomUUID().toString().replace("-", ""));
        drawing.setProjectId(dto.getProjectId());
        drawing.setName(dto.getName());
        drawing.setFilePath(dto.getFilePath());
        drawing.setFileFormat(dto.getFileFormat());
        drawing.setVersion(dto.getVersion() != null ? dto.getVersion() : "1.0");
        drawing.setStatus(dto.getStatus() != null ? dto.getStatus() : "DRAFT");
        drawing.setThumbnail(dto.getThumbnail());
        drawing.setCreatedBy(dto.getCreatedBy());
        return drawingRepository.save(drawing);
    }

    @Override
    public Structure2dDrawing get2dDrawingById(String drawingId) {
        return drawingRepository.findById(drawingId).orElse(null);
    }

    @Override
    public List<Structure2dDrawing> get2dDrawingsByProject(String projectId) {
        return drawingRepository.findByProjectId(projectId);
    }

    @Override
    public Structure2dDrawing update2dDrawing(String drawingId, Structure2dDrawingDTO dto) {
        Structure2dDrawing drawing = drawingRepository.findById(drawingId).orElse(null);
        if (drawing != null) {
            if (dto.getName() != null) drawing.setName(dto.getName());
            if (dto.getFilePath() != null) drawing.setFilePath(dto.getFilePath());
            if (dto.getFileFormat() != null) drawing.setFileFormat(dto.getFileFormat());
            if (dto.getVersion() != null) drawing.setVersion(dto.getVersion());
            if (dto.getStatus() != null) drawing.setStatus(dto.getStatus());
            if (dto.getThumbnail() != null) drawing.setThumbnail(dto.getThumbnail());
            return drawingRepository.save(drawing);
        }
        return null;
    }

    @Override
    public void delete2dDrawing(String drawingId) {
        drawingRepository.deleteById(drawingId);
    }
}
