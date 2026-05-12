
package com.example.alm.controller;

import com.example.alm.dto.*;
import com.example.alm.entity.*;
import com.example.alm.service.StructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 结构工程师管理API控制器
 */
@RestController
@RequestMapping("/api/structure")
@CrossOrigin(origins = "*")
public class StructureController {

    @Autowired
    private StructureService structureService;

    // ========== 结构项目管理 ==========

    @GetMapping("/projects")
    public ResponseEntity<List<StructureProject>> getProjects() {
        return ResponseEntity.ok(structureService.getAllProjects());
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<StructureProject> getProject(@PathVariable String id) {
        StructureProject project = structureService.getProjectById(id);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project);
    }

    @PostMapping("/projects")
    public ResponseEntity<StructureProject> createProject(@RequestBody StructureProjectDTO dto) {
        return ResponseEntity.ok(structureService.createProject(dto));
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<StructureProject> updateProject(@PathVariable String id, @RequestBody StructureProjectDTO dto) {
        StructureProject project = structureService.updateProject(id, dto);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        structureService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    // ========== 3D模型管理 ==========

    @GetMapping("/3d-models")
    public ResponseEntity<List<Structure3dModel>> get3dModels(@RequestParam(required = false) String projectId) {
        if (projectId != null) {
            return ResponseEntity.ok(structureService.get3dModelsByProject(projectId));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/3d-models/{id}")
    public ResponseEntity<Structure3dModel> get3dModel(@PathVariable String id) {
        Structure3dModel model = structureService.get3dModelById(id);
        if (model == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(model);
    }

    @PostMapping("/3d-models")
    public ResponseEntity<Structure3dModel> create3dModel(@RequestBody Structure3dModelDTO dto) {
        return ResponseEntity.ok(structureService.create3dModel(dto));
    }

    @PutMapping("/3d-models/{id}")
    public ResponseEntity<Structure3dModel> update3dModel(@PathVariable String id, @RequestBody Structure3dModelDTO dto) {
        Structure3dModel model = structureService.update3dModel(id, dto);
        if (model == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/3d-models/{id}")
    public ResponseEntity<Void> delete3dModel(@PathVariable String id) {
        structureService.delete3dModel(id);
        return ResponseEntity.noContent().build();
    }

    // ========== 2D图纸管理 ==========

    @GetMapping("/2d-drawings")
    public ResponseEntity<List<Structure2dDrawing>> get2dDrawings(@RequestParam(required = false) String projectId) {
        if (projectId != null) {
            return ResponseEntity.ok(structureService.get2dDrawingsByProject(projectId));
        }
        return ResponseEntity.ok(List.of());
    }

    @GetMapping("/2d-drawings/{id}")
    public ResponseEntity<Structure2dDrawing> get2dDrawing(@PathVariable String id) {
        Structure2dDrawing drawing = structureService.get2dDrawingById(id);
        if (drawing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(drawing);
    }

    @PostMapping("/2d-drawings")
    public ResponseEntity<Structure2dDrawing> create2dDrawing(@RequestBody Structure2dDrawingDTO dto) {
        return ResponseEntity.ok(structureService.create2dDrawing(dto));
    }

    @PutMapping("/2d-drawings/{id}")
    public ResponseEntity<Structure2dDrawing> update2dDrawing(@PathVariable String id, @RequestBody Structure2dDrawingDTO dto) {
        Structure2dDrawing drawing = structureService.update2dDrawing(id, dto);
        if (drawing == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(drawing);
    }

    @DeleteMapping("/2d-drawings/{id}")
    public ResponseEntity<Void> delete2dDrawing(@PathVariable String id) {
        structureService.delete2dDrawing(id);
        return ResponseEntity.noContent().build();
    }
}
