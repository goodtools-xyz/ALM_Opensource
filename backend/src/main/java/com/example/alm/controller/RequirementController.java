package com.example.alm.controller;

import com.example.alm.entity.Requirement;
import com.example.alm.service.DocumentParseService;
import com.example.alm.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requirement")
@CrossOrigin(origins = "*")
public class RequirementController {

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private DocumentParseService documentParseService;

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

    /**
     * 导入文档解析需求（预览模式，不保存）
     * @param file Word或Excel文档
     * @return 解析出的需求列表
     */
    @PostMapping("/import/preview")
    public Map<String, Object> importDocumentPreview(@RequestParam("file") MultipartFile file) {
        List<Requirement> requirements = documentParseService.parseDocument(file);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("count", requirements.size());
        result.put("data", requirements);
        return result;
    }

    /**
     * 导入文档解析需求（保存模式）
     * @param file Word或Excel文档
     * @return 导入结果
     */
    @PostMapping("/import/save")
    public Map<String, Object> importDocumentSave(@RequestParam("file") MultipartFile file) {
        List<Requirement> requirements = documentParseService.parseDocument(file);
        
        int successCount = 0;
        int failCount = 0;
        
        for (Requirement req : requirements) {
            try {
                requirementService.createRequirement(req);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("total", requirements.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        return result;
    }
}