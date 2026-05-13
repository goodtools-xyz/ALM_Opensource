package com.example.alm.repository;

import com.example.alm.entity.Defect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DefectRepository extends JpaRepository<Defect, Long> {

    Optional<Defect> findByDefectId(String defectId);

    List<Defect> findByProjectId(String projectId);

    List<Defect> findByStatus(String status);

    List<Defect> findBySeverity(String severity);

    List<Defect> findByPriority(String priority);

    List<Defect> findByRelatedReqId(String reqId);

    List<Defect> findByRelatedTestCaseId(String caseId);

    List<Defect> findByAssignee(String assignee);

    List<Defect> findByReporter(String reporter);

    List<Defect> findByTitleContaining(String keyword);

    boolean existsByDefectId(String defectId);
}