package com.example.alm.service.impl;

import com.example.alm.entity.TestCase;
import com.example.alm.repository.TestCaseRepository;
import com.example.alm.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TestCaseServiceImpl implements TestCaseService {

    @Autowired
    private TestCaseRepository testCaseRepository;

    @Override
    public TestCase createTestCase(TestCase testCase) {
        if (testCase.getCaseId() == null || testCase.getCaseId().isEmpty()) {
            testCase.setCaseId(generateCaseId());
        }
        testCase.setCreatedAt(LocalDateTime.now());
        testCase.setUpdatedAt(LocalDateTime.now());
        return testCaseRepository.save(testCase);
    }

    @Override
    public TestCase updateTestCase(Long id, TestCase testCase) {
        TestCase existing = testCaseRepository.findById(id).orElseThrow();
        existing.setTitle(testCase.getTitle());
        existing.setDescription(testCase.getDescription());
        existing.setPreconditions(testCase.getPreconditions());
        existing.setSteps(testCase.getSteps());
        existing.setExpectedResult(testCase.getExpectedResult());
        existing.setType(testCase.getType());
        existing.setPriority(testCase.getPriority());
        existing.setStatus(testCase.getStatus());
        existing.setRelatedReqId(testCase.getRelatedReqId());
        existing.setRelatedDesignId(testCase.getRelatedDesignId());
        existing.setProjectId(testCase.getProjectId());
        existing.setModule(testCase.getModule());
        existing.setTestEngineer(testCase.getTestEngineer());
        existing.setUpdatedAt(LocalDateTime.now());
        return testCaseRepository.save(existing);
    }

    @Override
    public void deleteTestCase(Long id) {
        testCaseRepository.deleteById(id);
    }

    @Override
    public TestCase getTestCaseById(Long id) {
        return testCaseRepository.findById(id).orElse(null);
    }

    @Override
    public TestCase getTestCaseByCaseId(String caseId) {
        return testCaseRepository.findByCaseId(caseId).orElse(null);
    }

    @Override
    public List<TestCase> getAllTestCases() {
        return testCaseRepository.findAll();
    }

    @Override
    public List<TestCase> getTestCasesByProjectId(String projectId) {
        return testCaseRepository.findByProjectId(projectId);
    }

    @Override
    public List<TestCase> getTestCasesByStatus(String status) {
        return testCaseRepository.findByStatus(status);
    }

    @Override
    public List<TestCase> getTestCasesByReqId(String reqId) {
        return testCaseRepository.findByRelatedReqId(reqId);
    }

    @Override
    public List<TestCase> getTestCasesByDesignId(String designId) {
        return testCaseRepository.findByRelatedDesignId(designId);
    }

    @Override
    public List<TestCase> searchTestCases(String keyword) {
        return testCaseRepository.findAll().stream()
                .filter(tc -> tc.getTitle().contains(keyword) ||
                        tc.getCaseId().contains(keyword) ||
                        (tc.getDescription() != null && tc.getDescription().contains(keyword)))
                .collect(Collectors.toList());
    }

    @Override
    public String generateCaseId() {
        long count = testCaseRepository.count() + 1;
        return String.format("TEST-%04d", count);
    }
}