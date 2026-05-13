package com.example.alm.service.impl;

import com.example.alm.entity.Defect;
import com.example.alm.repository.DefectRepository;
import com.example.alm.service.DefectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefectServiceImpl implements DefectService {

    @Autowired
    private DefectRepository defectRepository;

    @Override
    public Defect createDefect(Defect defect) {
        if (defect.getDefectId() == null || defect.getDefectId().isEmpty()) {
            defect.setDefectId(generateDefectId());
        }
        defect.setCreatedAt(LocalDateTime.now());
        defect.setUpdatedAt(LocalDateTime.now());
        return defectRepository.save(defect);
    }

    @Override
    public Defect updateDefect(Long id, Defect defect) {
        Defect existing = defectRepository.findById(id).orElseThrow();
        existing.setTitle(defect.getTitle());
        existing.setDescription(defect.getDescription());
        existing.setSeverity(defect.getSeverity());
        existing.setPriority(defect.getPriority());
        existing.setStatus(defect.getStatus());
        existing.setType(defect.getType());
        existing.setReproduceSteps(defect.getReproduceSteps());
        existing.setRelatedReqId(defect.getRelatedReqId());
        existing.setRelatedTestCaseId(defect.getRelatedTestCaseId());
        existing.setProjectId(defect.getProjectId());
        existing.setModule(defect.getModule());
        existing.setReporter(defect.getReporter());
        existing.setAssignee(defect.getAssignee());
        existing.setUpdatedAt(LocalDateTime.now());
        return defectRepository.save(existing);
    }

    @Override
    public void deleteDefect(Long id) {
        defectRepository.deleteById(id);
    }

    @Override
    public Defect getDefectById(Long id) {
        return defectRepository.findById(id).orElse(null);
    }

    @Override
    public Defect getDefectByDefectId(String defectId) {
        return defectRepository.findByDefectId(defectId).orElse(null);
    }

    @Override
    public List<Defect> getAllDefects() {
        return defectRepository.findAll();
    }

    @Override
    public List<Defect> getDefectsByProjectId(String projectId) {
        return defectRepository.findByProjectId(projectId);
    }

    @Override
    public List<Defect> getDefectsByStatus(String status) {
        return defectRepository.findByStatus(status);
    }

    @Override
    public List<Defect> getDefectsBySeverity(String severity) {
        return defectRepository.findBySeverity(severity);
    }

    @Override
    public List<Defect> getDefectsByPriority(String priority) {
        return defectRepository.findByPriority(priority);
    }

    @Override
    public List<Defect> getDefectsByReqId(String reqId) {
        return defectRepository.findByRelatedReqId(reqId);
    }

    @Override
    public List<Defect> getDefectsByTestCaseId(String caseId) {
        return defectRepository.findByRelatedTestCaseId(caseId);
    }

    @Override
    public List<Defect> getDefectsByAssignee(String assignee) {
        return defectRepository.findByAssignee(assignee);
    }

    @Override
    public List<Defect> searchDefects(String keyword) {
        return defectRepository.findAll().stream()
                .filter(d -> d.getTitle().contains(keyword) ||
                        d.getDefectId().contains(keyword) ||
                        (d.getDescription() != null && d.getDescription().contains(keyword)))
                .collect(Collectors.toList());
    }

    @Override
    public String generateDefectId() {
        long count = defectRepository.count() + 1;
        return String.format("DEF-%04d", count);
    }

    @Override
    public Defect resolveDefect(Long id, String resolvedBy) {
        Defect existing = defectRepository.findById(id).orElseThrow();
        existing.setStatus("RESOLVED");
        existing.setResolvedBy(resolvedBy);
        existing.setResolvedAt(LocalDateTime.now());
        existing.setUpdatedAt(LocalDateTime.now());
        return defectRepository.save(existing);
    }
}