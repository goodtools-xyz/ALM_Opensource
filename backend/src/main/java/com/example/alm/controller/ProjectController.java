package com.example.alm.controller;

import com.example.alm.entity.Project;
import com.example.alm.entity.FileFolder;
import com.example.alm.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }
    
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable String projectId) {
        Project project = projectService.getProjectById(projectId);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/code/{code}")
    public ResponseEntity<Project> getProjectByCode(@PathVariable String code) {
        Project project = projectService.getProjectByCode(code);
        return project != null ? ResponseEntity.ok(project) : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.createProject(project));
    }
    
    @PutMapping("/{projectId}")
    public ResponseEntity<Project> updateProject(@PathVariable String projectId, @RequestBody Project project) {
        Project updated = projectService.updateProject(projectId, project);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable String projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{projectId}/folder")
    public ResponseEntity<FileFolder> getProjectRootFolder(@PathVariable String projectId) {
        FileFolder folder = projectService.getProjectRootFolder(projectId);
        return folder != null ? ResponseEntity.ok(folder) : ResponseEntity.notFound().build();
    }
}
