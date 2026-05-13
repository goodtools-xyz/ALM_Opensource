package com.example.alm.service.impl;

import com.example.alm.entity.Requirement;
import com.example.alm.repository.RequirementRepository;
import com.example.alm.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequirementServiceImpl implements RequirementService {

    @Autowired
    private RequirementRepository requirementRepository;

    @Override
    public Requirement createRequirement(Requirement requirement) {
        if (requirement.getReqId() == null || requirement.getReqId().isEmpty()) {
            requirement.setReqId(generateReqId());
        }
        requirement.setCreatedAt(LocalDateTime.now());
        requirement.setUpdatedAt(LocalDateTime.now());
        return requirementRepository.save(requirement);
    }

    @Override
    public Requirement updateRequirement(Long id, Requirement requirement) {
        Requirement existing = requirementRepository.findById(id).orElseThrow();
        existing.setTitle(requirement.getTitle());
        existing.setDescription(requirement.getDescription());
        existing.setPriority(requirement.getPriority());
        existing.setStatus(requirement.getStatus());
        existing.setType(requirement.getType());
        existing.setSource(requirement.getSource());
        existing.setVersion(requirement.getVersion());
        existing.setProjectId(requirement.getProjectId());
        existing.setModule(requirement.getModule());
        existing.setOwner(requirement.getOwner());
        existing.setUpdatedAt(LocalDateTime.now());
        return requirementRepository.save(existing);
    }

    @Override
    public void deleteRequirement(Long id) {
        requirementRepository.deleteById(id);
    }

    @Override
    public Requirement getRequirementById(Long id) {
        return requirementRepository.findById(id).orElse(null);
    }

    @Override
    public Requirement getRequirementByReqId(String reqId) {
        return requirementRepository.findByReqId(reqId).orElse(null);
    }

    @Override
    public List<Requirement> getAllRequirements() {
        return requirementRepository.findAll();
    }

    @Override
    public List<Requirement> getRequirementsByProjectId(String projectId) {
        return requirementRepository.findByProjectId(projectId);
    }

    @Override
    public List<Requirement> getRequirementsByStatus(String status) {
        return requirementRepository.findByStatus(status);
    }

    @Override
    public List<Requirement> searchRequirements(String keyword) {
        return requirementRepository.findAll().stream()
                .filter(req -> req.getTitle().contains(keyword) ||
                        req.getReqId().contains(keyword) ||
                        (req.getDescription() != null && req.getDescription().contains(keyword)))
                .collect(Collectors.toList());
    }

    @Override
    public String generateReqId() {
        long count = requirementRepository.count() + 1;
        return String.format("REQ-%04d", count);
    }

    @Override
    public long getRequirementsCount() {
        return requirementRepository.count();
    }
}