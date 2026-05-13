package com.example.alm.service;

import com.example.alm.entity.Defect;

import java.util.List;

public interface DefectService {

    Defect createDefect(Defect defect);

    Defect updateDefect(Long id, Defect defect);

    void deleteDefect(Long id);

    Defect getDefectById(Long id);

    Defect getDefectByDefectId(String defectId);

    List<Defect> getAllDefects();

    List<Defect> getDefectsByProjectId(String projectId);

    List<Defect> getDefectsByStatus(String status);

    List<Defect> getDefectsBySeverity(String severity);

    List<Defect> getDefectsByPriority(String priority);

    List<Defect> getDefectsByReqId(String reqId);

    List<Defect> getDefectsByTestCaseId(String caseId);

    List<Defect> getDefectsByAssignee(String assignee);

    List<Defect> searchDefects(String keyword);

    String generateDefectId();

    Defect resolveDefect(Long id, String resolvedBy);
}