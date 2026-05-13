package com.example.alm.service;

import com.example.alm.entity.Requirement;

import java.util.List;

public interface RequirementService {

    Requirement createRequirement(Requirement requirement);

    Requirement updateRequirement(Long id, Requirement requirement);

    void deleteRequirement(Long id);

    Requirement getRequirementById(Long id);

    Requirement getRequirementByReqId(String reqId);

    List<Requirement> getAllRequirements();

    List<Requirement> getRequirementsByProjectId(String projectId);

    List<Requirement> getRequirementsByStatus(String status);

    List<Requirement> searchRequirements(String keyword);

    String generateReqId();
}