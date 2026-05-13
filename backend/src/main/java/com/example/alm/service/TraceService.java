package com.example.alm.service;

import com.example.alm.dto.TraceMatrixDTO;
import com.example.alm.dto.TraceNodeDTO;

import java.util.List;
import java.util.Map;

public interface TraceService {

    Map<String, Object> getRequirementTrace(String reqId);

    Map<String, Object> getDesignTrace(String designId);

    Map<String, Object> getTestTrace(String caseId);

    Map<String, Object> getImpactAnalysis(String itemId);

    List<TraceMatrixDTO> getTraceMatrix(String projectId);

    void createRelation(String sourceType, String sourceId, String targetType, String targetId, String relationType);

    void deleteRelation(Long relationId);

    List<TraceNodeDTO> searchItems(String keyword, String type);
}