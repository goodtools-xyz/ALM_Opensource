package com.example.alm.controller;

import com.example.alm.entity.Defect;
import com.example.alm.service.DefectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/defect")
@CrossOrigin(origins = "*")
public class DefectController {

    @Autowired
    private DefectService defectService;

    @GetMapping
    public List<Defect> getAllDefects(
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String reqId,
            @RequestParam(required = false) String assignee,
            @RequestParam(required = false) String keyword) {
        if (projectId != null && !projectId.isEmpty()) {
            return defectService.getDefectsByProjectId(projectId);
        }
        if (status != null && !status.isEmpty()) {
            return defectService.getDefectsByStatus(status);
        }
        if (severity != null && !severity.isEmpty()) {
            return defectService.getDefectsBySeverity(severity);
        }
        if (priority != null && !priority.isEmpty()) {
            return defectService.getDefectsByPriority(priority);
        }
        if (reqId != null && !reqId.isEmpty()) {
            return defectService.getDefectsByReqId(reqId);
        }
        if (assignee != null && !assignee.isEmpty()) {
            return defectService.getDefectsByAssignee(assignee);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return defectService.searchDefects(keyword);
        }
        return defectService.getAllDefects();
    }

    @GetMapping("/{id}")
    public Defect getDefectById(@PathVariable Long id) {
        return defectService.getDefectById(id);
    }

    @GetMapping("/defect/{defectId}")
    public Defect getDefectByDefectId(@PathVariable String defectId) {
        return defectService.getDefectByDefectId(defectId);
    }

    @PostMapping
    public Defect createDefect(@RequestBody Defect defect) {
        return defectService.createDefect(defect);
    }

    @PutMapping("/{id}")
    public Defect updateDefect(@PathVariable Long id, @RequestBody Defect defect) {
        return defectService.updateDefect(id, defect);
    }

    @DeleteMapping("/{id}")
    public void deleteDefect(@PathVariable Long id) {
        defectService.deleteDefect(id);
    }

    @PostMapping("/{id}/resolve")
    public Defect resolveDefect(@PathVariable Long id, @RequestBody String resolvedBy) {
        return defectService.resolveDefect(id, resolvedBy);
    }

    @GetMapping("/generate-id")
    public String generateDefectId() {
        return defectService.generateDefectId();
    }
}