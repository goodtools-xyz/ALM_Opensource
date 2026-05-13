package com.example.alm.controller;

import com.example.alm.entity.Requirement;
import com.example.alm.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirement")
@CrossOrigin(origins = "*")
public class RequirementController {

    @Autowired
    private RequirementService requirementService;

    @GetMapping
    public List<Requirement> getAllRequirements(
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        if (projectId != null && !projectId.isEmpty()) {
            return requirementService.getRequirementsByProjectId(projectId);
        }
        if (status != null && !status.isEmpty()) {
            return requirementService.getRequirementsByStatus(status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return requirementService.searchRequirements(keyword);
        }
        return requirementService.getAllRequirements();
    }

    @GetMapping("/{id}")
    public Requirement getRequirementById(@PathVariable Long id) {
        return requirementService.getRequirementById(id);
    }

    @GetMapping("/req/{reqId}")
    public Requirement getRequirementByReqId(@PathVariable String reqId) {
        return requirementService.getRequirementByReqId(reqId);
    }

    @PostMapping
    public Requirement createRequirement(@RequestBody Requirement requirement) {
        return requirementService.createRequirement(requirement);
    }

    @PutMapping("/{id}")
    public Requirement updateRequirement(@PathVariable Long id, @RequestBody Requirement requirement) {
        return requirementService.updateRequirement(id, requirement);
    }

    @DeleteMapping("/{id}")
    public void deleteRequirement(@PathVariable Long id) {
        requirementService.deleteRequirement(id);
    }

    @GetMapping("/generate-id")
    public String generateReqId() {
        return requirementService.generateReqId();
    }
}