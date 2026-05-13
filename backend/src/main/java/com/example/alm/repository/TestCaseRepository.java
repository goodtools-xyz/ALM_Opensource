package com.example.alm.repository;

import com.example.alm.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    Optional<TestCase> findByCaseId(String caseId);

    List<TestCase> findByProjectId(String projectId);

    List<TestCase> findByStatus(String status);

    List<TestCase> findByPriority(String priority);

    List<TestCase> findByRelatedReqId(String reqId);

    List<TestCase> findByRelatedDesignId(String designId);

    List<TestCase> findByType(String type);

    List<TestCase> findByTitleContaining(String keyword);

    boolean existsByCaseId(String caseId);
}