
package com.example.alm.service;

import com.example.alm.dto.*;
import com.example.alm.entity.*;

import java.util.List;

/**
 * 硬件电子工程师服务接口
 */
public interface HardwareService {

    // 硬件项目管理
    HardwareProject createProject(HardwareProjectDTO dto);
    HardwareProject getProjectById(String projectId);
    List<HardwareProject> getAllProjects();
    HardwareProject updateProject(String projectId, HardwareProjectDTO dto);
    void deleteProject(String projectId);

    // 原理图管理
    HardwareSchematic createSchematic(HardwareSchematicDTO dto);
    HardwareSchematic getSchematicById(String schematicId);
    List<HardwareSchematic> getSchematicsByProject(String projectId);
    HardwareSchematic updateSchematic(String schematicId, HardwareSchematicDTO dto);
    void deleteSchematic(String schematicId);

    // PCB设计管理
    HardwarePcb createPcb(HardwarePcbDTO dto);
    HardwarePcb getPcbById(String pcbId);
    List<HardwarePcb> getPcbsByProject(String projectId);
    HardwarePcb updatePcb(String pcbId, HardwarePcbDTO dto);
    void deletePcb(String pcbId);
}
