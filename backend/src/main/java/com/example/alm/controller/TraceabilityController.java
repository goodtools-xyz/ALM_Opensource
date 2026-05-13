package com.example.alm.controller;

import com.example.alm.dto.TraceMatrixDTO;
import com.example.alm.dto.TraceNodeDTO;
import com.example.alm.service.TraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/traceability")
@CrossOrigin(origins = "*")
public class TraceabilityController {

    @Autowired
    private TraceService traceService;

    @GetMapping("/requirement/{reqId}")
    public Map<String, Object> getRequirementTrace(@PathVariable String reqId) {
        return traceService.getRequirementTrace(reqId);
    }

    @GetMapping("/design/{designId}")
    public Map<String, Object> getDesignTrace(@PathVariable String designId) {
        return traceService.getDesignTrace(designId);
    }

    @GetMapping("/test/{caseId}")
    public Map<String, Object> getTestTrace(@PathVariable String caseId) {
        return traceService.getTestTrace(caseId);
    }

    @GetMapping("/impact/{itemId}")
    public Map<String, Object> getImpactAnalysis(@PathVariable String itemId) {
        return traceService.getImpactAnalysis(itemId);
    }

    @GetMapping("/matrix")
    public List<TraceMatrixDTO> getTraceMatrix(@RequestParam(required = false) String projectId) {
        return traceService.getTraceMatrix(projectId);
    }

    @GetMapping("/search")
    public List<TraceNodeDTO> searchItems(@RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String type) {
        return traceService.searchItems(keyword, type);
    }

    @PostMapping("/relation")
    public void createRelation(@RequestBody Map<String, String> request) {
        String sourceType = request.get("sourceType");
        String sourceId = request.get("sourceId");
        String targetType = request.get("targetType");
        String targetId = request.get("targetId");
        String relationType = request.get("relationType");
        traceService.createRelation(sourceType, sourceId, targetType, targetId, relationType);
    }

    @DeleteMapping("/relation/{id}")
    public void deleteRelation(@PathVariable Long id) {
        traceService.deleteRelation(id);
    }
}