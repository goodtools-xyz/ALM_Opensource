package com.example.alm.service.impl;

import com.example.alm.dto.TraceMatrixDTO;
import com.example.alm.dto.TraceNodeDTO;
import com.example.alm.entity.TraceRelation;
import com.example.alm.repository.TraceRelationRepository;
import com.example.alm.service.TraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TraceServiceImpl implements TraceService {

    @Autowired
    private TraceRelationRepository traceRelationRepository;

    @Override
    public Map<String, Object> getRequirementTrace(String reqId) {
        Map<String, Object> result = new HashMap<>();
        result.put("reqId", reqId);

        List<Map<String, Object>> upstream = new ArrayList<>();
        List<Map<String, Object>> downstream = new ArrayList<>();

        List<TraceRelation> asSource = traceRelationRepository.findBySourceTypeAndSourceId("REQ", reqId);
        for (TraceRelation relation : asSource) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", relation.getTargetId());
            item.put("type", relation.getTargetType());
            item.put("relationType", relation.getRelationType());
            downstream.add(item);
        }

        List<TraceRelation> asTarget = traceRelationRepository.findByTargetTypeAndTargetId("REQ", reqId);
        for (TraceRelation relation : asTarget) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", relation.getSourceId());
            item.put("type", relation.getSourceType());
            item.put("relationType", relation.getRelationType());
            upstream.add(item);
        }

        result.put("upstream", upstream);
        result.put("downstream", downstream);
        return result;
    }

    @Override
    public Map<String, Object> getDesignTrace(String designId) {
        Map<String, Object> result = new HashMap<>();
        result.put("designId", designId);

        List<Map<String, Object>> upstream = new ArrayList<>();
        List<Map<String, Object>> downstream = new ArrayList<>();

        List<TraceRelation> asSource = traceRelationRepository.findBySourceTypeAndSourceId("DESIGN", designId);
        for (TraceRelation relation : asSource) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", relation.getTargetId());
            item.put("type", relation.getTargetType());
            item.put("relationType", relation.getRelationType());
            downstream.add(item);
        }

        List<TraceRelation> asTarget = traceRelationRepository.findByTargetTypeAndTargetId("DESIGN", designId);
        for (TraceRelation relation : asTarget) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", relation.getSourceId());
            item.put("type", relation.getSourceType());
            item.put("relationType", relation.getRelationType());
            upstream.add(item);
        }

        result.put("upstream", upstream);
        result.put("downstream", downstream);
        return result;
    }

    @Override
    public Map<String, Object> getTestTrace(String caseId) {
        Map<String, Object> result = new HashMap<>();
        result.put("caseId", caseId);

        List<Map<String, Object>> upstream = new ArrayList<>();
        List<Map<String, Object>> downstream = new ArrayList<>();

        List<TraceRelation> asSource = traceRelationRepository.findBySourceTypeAndSourceId("TEST", caseId);
        for (TraceRelation relation : asSource) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", relation.getTargetId());
            item.put("type", relation.getTargetType());
            item.put("relationType", relation.getRelationType());
            downstream.add(item);
        }

        List<TraceRelation> asTarget = traceRelationRepository.findByTargetTypeAndTargetId("TEST", caseId);
        for (TraceRelation relation : asTarget) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", relation.getSourceId());
            item.put("type", relation.getSourceType());
            item.put("relationType", relation.getRelationType());
            upstream.add(item);
        }

        result.put("upstream", upstream);
        result.put("downstream", downstream);
        return result;
    }

    @Override
    public Map<String, Object> getImpactAnalysis(String itemId) {
        Map<String, Object> result = new HashMap<>();
        Set<String> impactedItems = new HashSet<>();

        List<TraceRelation> relations = traceRelationRepository.findBySourceTypeAndSourceIdOrTargetTypeAndTargetId(
                "REQ", itemId, "REQ", itemId);
        for (TraceRelation relation : relations) {
            impactedItems.add(relation.getTargetId());
        }

        relations = traceRelationRepository.findBySourceTypeAndSourceIdOrTargetTypeAndTargetId(
                "DESIGN", itemId, "DESIGN", itemId);
        for (TraceRelation relation : relations) {
            impactedItems.add(relation.getTargetId());
        }

        relations = traceRelationRepository.findBySourceTypeAndSourceIdOrTargetTypeAndTargetId(
                "TEST", itemId, "TEST", itemId);
        for (TraceRelation relation : relations) {
            impactedItems.add(relation.getTargetId());
        }

        result.put("itemId", itemId);
        result.put("impactedItems", new ArrayList<>(impactedItems));
        result.put("impactCount", impactedItems.size());
        return result;
    }

    @Override
    public List<TraceMatrixDTO> getTraceMatrix(String projectId) {
        List<TraceRelation> relations;
        if (projectId == null || projectId.isEmpty()) {
            relations = traceRelationRepository.findAll();
        } else {
            relations = traceRelationRepository.findByProjectId(projectId);
        }

        Map<String, TraceMatrixDTO> matrixMap = new HashMap<>();

        for (TraceRelation relation : relations) {
            if ("REQ".equals(relation.getSourceType())) {
                String reqId = relation.getSourceId();
                matrixMap.putIfAbsent(reqId, new TraceMatrixDTO(reqId, reqId, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

                TraceMatrixDTO dto = matrixMap.get(reqId);
                if ("DESIGN".equals(relation.getTargetType())) {
                    dto.getDesigns().add(new TraceMatrixDTO.DesignItem(relation.getTargetId(), relation.getTargetId(), true));
                } else if ("TEST".equals(relation.getTargetType())) {
                    dto.getTests().add(new TraceMatrixDTO.TestItem(relation.getTargetId(), relation.getTargetId(), true));
                } else if ("DEFECT".equals(relation.getTargetType())) {
                    dto.getDefects().add(new TraceMatrixDTO.DefectItem(relation.getTargetId(), relation.getTargetId(), true));
                }
            }
        }

        return new ArrayList<>(matrixMap.values());
    }

    @Override
    public void createRelation(String sourceType, String sourceId, String targetType, String targetId, String relationType) {
        TraceRelation relation = new TraceRelation();
        relation.setSourceType(sourceType);
        relation.setSourceId(sourceId);
        relation.setTargetType(targetType);
        relation.setTargetId(targetId);
        relation.setRelationType(relationType);
        traceRelationRepository.save(relation);
    }

    @Override
    public void deleteRelation(Long relationId) {
        traceRelationRepository.deleteById(relationId);
    }

    @Override
    public List<TraceNodeDTO> searchItems(String keyword, String type) {
        List<TraceNodeDTO> results = new ArrayList<>();

        if (type == null || type.isEmpty() || "REQ".equals(type)) {
            results.add(new TraceNodeDTO("REQ-001", "需求项1", "REQ", "ACTIVE", "示例需求"));
            results.add(new TraceNodeDTO("REQ-002", "需求项2", "REQ", "ACTIVE", "示例需求"));
        }
        if (type == null || type.isEmpty() || "DESIGN".equals(type)) {
            results.add(new TraceNodeDTO("DES-001", "设计项1", "DESIGN", "ACTIVE", "示例设计"));
            results.add(new TraceNodeDTO("DES-002", "设计项2", "DESIGN", "ACTIVE", "示例设计"));
        }
        if (type == null || type.isEmpty() || "TEST".equals(type)) {
            results.add(new TraceNodeDTO("TEST-001", "测试用例1", "TEST", "ACTIVE", "示例测试"));
            results.add(new TraceNodeDTO("TEST-002", "测试用例2", "TEST", "ACTIVE", "示例测试"));
        }
        if (type == null || type.isEmpty() || "DEFECT".equals(type)) {
            results.add(new TraceNodeDTO("DEF-001", "缺陷项1", "DEFECT", "OPEN", "示例缺陷"));
            results.add(new TraceNodeDTO("DEF-002", "缺陷项2", "DEFECT", "CLOSED", "示例缺陷"));
        }

        return results.stream()
                .filter(item -> keyword == null || keyword.isEmpty() ||
                        item.getName().contains(keyword) ||
                        item.getId().contains(keyword))
                .toList();
    }
}