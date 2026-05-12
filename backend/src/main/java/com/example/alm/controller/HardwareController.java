
package com.example.alm.controller;

import com.example.alm.dto.*;
import com.example.alm.entity.*;
import com.example.alm.service.HardwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 硬件电子工程师管理API控制器
 */
@RestController
@RequestMapping("/api/hardware")
@CrossOrigin(origins = "*")
public class HardwareController {

    @Autowired
    private HardwareService hardwareService;

    // ========== 硬件项目管理 ==========

    @GetMapping("/projects")
    public ResponseEntity<List<HardwareProject>> getProjects() {
        return ResponseEntity.ok(hardwareService.getAllProjects());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<HardwareProject> getProject(@PathVariable String id) {
        HardwareProject project = hardwareService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project);
    }

    @PostMapping("/projects")
    public ResponseEntity<HardwareProject> createProject(@RequestBody HardwareProjectDTO dto) {
        return ResponseEntity.ok(hardwareService.createProject(dto));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<HardwareProject> updateProject(@PathVariable String id, @RequestBody HardwareProjectDTO dto) {
        HardwareProject project = hardwareService.updateProject(id, dto);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        hardwareService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // ========== 原理图管理 ==========

    @GetMapping("/schematics")
    public ResponseEntity<List<HardwareSchematic>> getSchematics(@RequestParam(required = false) String projectId) {
        if (projectId != null) {
            return ResponseEntity.ok(hardwareService.getSchematicsByProject(projectId));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/schematics/{id}")
    public ResponseEntity<HardwareSchematic> getSchematic(@PathVariable String id) {
        HardwareSchematic schematic = hardwareService.getSchematicById(id);
        if (schematic == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(schematic);
    }

    @PostMapping("/schematics")
    public ResponseEntity<HardwareSchematic> createSchematic(@RequestBody HardwareSchematicDTO dto) {
        return ResponseEntity.ok(hardwareService.createSchematic(dto));
    }

    @PutMapping("/schematics/{id}")
    public ResponseEntity<HardwareSchematic> updateSchematic(@PathVariable String id, @RequestBody HardwareSchematicDTO dto) {
        HardwareSchematic schematic = hardwareService.updateSchematic(id, dto);
        if (schematic == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(schematic);
    }

    @DeleteMapping("/schematics/{id}")
    public ResponseEntity<Void> deleteSchematic(@PathVariable String id) {
        hardwareService.deleteSchematic(id);
        return ResponseEntity.noContent().build();
    }

    // ========== PCB设计管理 ==========

    @GetMapping("/pcbs")
    public ResponseEntity<List<HardwarePcb>> getPcbs(@RequestParam(required = false) String projectId) {
        if (projectId != null) {
            return ResponseEntity.ok(hardwareService.getPcbsByProject(projectId));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/pcbs/{id}")
    public ResponseEntity<HardwarePcb> getPcb(@PathVariable String id) {
        HardwarePcb pcb = hardwareService.getPcbById(id);
        if (pcb == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pcb);
    }

    @PostMapping("/pcbs")
    public ResponseEntity<HardwarePcb> createPcb(@RequestBody HardwarePcbDTO dto) {
        return ResponseEntity.ok(hardwareService.createPcb(dto));
    }

    @PutMapping("/pcbs/{id}")
    public ResponseEntity<HardwarePcb> updatePcb(@PathVariable String id, @RequestBody HardwarePcbDTO dto) {
        HardwarePcb pcb = hardwareService.updatePcb(id, dto);
        if (pcb == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pcb);
    }

    @DeleteMapping("/pcbs/{id}")
    public ResponseEntity<Void> deletePcb(@PathVariable String id) {
        hardwareService.deletePcb(id);
        return ResponseEntity.noContent().build();
    }
}
