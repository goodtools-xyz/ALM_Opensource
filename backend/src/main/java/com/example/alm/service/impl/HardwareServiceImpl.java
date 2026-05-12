
package com.example.alm.service.impl;

import com.example.alm.dto.*;
import com.example.alm.entity.*;
import com.example.alm.repository.*;
import com.example.alm.service.HardwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 硬件电子工程师服务实现
 */
@Service
public class HardwareServiceImpl implements HardwareService {

    @Autowired
    private HardwareProjectRepository projectRepository;

    @Autowired
    private HardwareSchematicRepository schematicRepository;

    @Autowired
    private HardwarePcbRepository pcbRepository;

    @Override
    public HardwareProject createProject(HardwareProjectDTO dto) {
        HardwareProject project = new HardwareProject();
        project.setProjectId(UUID.randomUUID().toString().replace("-", ""));
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setStatus(dto.getStatus() != null ? dto.getStatus() : "PLANNING");
        project.setCreatedBy(dto.getCreatedBy());
        return projectRepository.save(project);
    }

    @Override
    public HardwareProject getProjectById(String projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }

    @Override
    public List<HardwareProject> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public HardwareProject updateProject(String projectId, HardwareProjectDTO dto) {
        HardwareProject project = projectRepository.findById(projectId).orElse(null);
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
    public HardwareSchematic createSchematic(HardwareSchematicDTO dto) {
        HardwareSchematic schematic = new HardwareSchematic();
        schematic.setSchematicId(UUID.randomUUID().toString().replace("-", ""));
        schematic.setProjectId(dto.getProjectId());
        schematic.setName(dto.getName());
        schematic.setFilePath(dto.getFilePath());
        schematic.setFileType(dto.getFileType());
        schematic.setVersion(dto.getVersion() != null ? dto.getVersion() : "1.0");
        schematic.setStatus(dto.getStatus() != null ? dto.getStatus() : "DRAFT");
        schematic.setThumbnail(dto.getThumbnail());
        schematic.setCreatedBy(dto.getCreatedBy());
        return schematicRepository.save(schematic);
    }

    @Override
    public HardwareSchematic getSchematicById(String schematicId) {
        return schematicRepository.findById(schematicId).orElse(null);
    }

    @Override
    public List<HardwareSchematic> getSchematicsByProject(String projectId) {
        return schematicRepository.findByProjectId(projectId);
    }

    @Override
    public HardwareSchematic updateSchematic(String schematicId, HardwareSchematicDTO dto) {
        HardwareSchematic schematic = schematicRepository.findById(schematicId).orElse(null);
        if (schematic != null) {
            if (dto.getName() != null) schematic.setName(dto.getName());
            if (dto.getFilePath() != null) schematic.setFilePath(dto.getFilePath());
            if (dto.getFileType() != null) schematic.setFileType(dto.getFileType());
            if (dto.getVersion() != null) schematic.setVersion(dto.getVersion());
            if (dto.getStatus() != null) schematic.setStatus(dto.getStatus());
            if (dto.getThumbnail() != null) schematic.setThumbnail(dto.getThumbnail());
            return schematicRepository.save(schematic);
        }
        return null;
    }

    @Override
    public void deleteSchematic(String schematicId) {
        schematicRepository.deleteById(schematicId);
    }

    @Override
    public HardwarePcb createPcb(HardwarePcbDTO dto) {
        HardwarePcb pcb = new HardwarePcb();
        pcb.setPcbId(UUID.randomUUID().toString().replace("-", ""));
        pcb.setProjectId(dto.getProjectId());
        pcb.setName(dto.getName());
        pcb.setFilePath(dto.getFilePath());
        pcb.setLayers(dto.getLayers());
        pcb.setSize(dto.getSize());
        pcb.setVersion(dto.getVersion() != null ? dto.getVersion() : "1.0");
        pcb.setStatus(dto.getStatus() != null ? dto.getStatus() : "DRAFT");
        pcb.setThumbnail(dto.getThumbnail());
        pcb.setCreatedBy(dto.getCreatedBy());
        return pcbRepository.save(pcb);
    }

    @Override
    public HardwarePcb getPcbById(String pcbId) {
        return pcbRepository.findById(pcbId).orElse(null);
    }

    @Override
    public List<HardwarePcb> getPcbsByProject(String projectId) {
        return pcbRepository.findByProjectId(projectId);
    }

    @Override
    public HardwarePcb updatePcb(String pcbId, HardwarePcbDTO dto) {
        HardwarePcb pcb = pcbRepository.findById(pcbId).orElse(null);
        if (pcb != null) {
            if (dto.getName() != null) pcb.setName(dto.getName());
            if (dto.getFilePath() != null) pcb.setFilePath(dto.getFilePath());
            if (dto.getLayers() != null) pcb.setLayers(dto.getLayers());
            if (dto.getSize() != null) pcb.setSize(dto.getSize());
            if (dto.getVersion() != null) pcb.setVersion(dto.getVersion());
            if (dto.getStatus() != null) pcb.setStatus(dto.getStatus());
            if (dto.getThumbnail() != null) pcb.setThumbnail(dto.getThumbnail());
            return pcbRepository.save(pcb);
        }
        return null;
    }

    @Override
    public void deletePcb(String pcbId) {
        pcbRepository.deleteById(pcbId);
    }
}
