package com.example.alm.service;

import com.example.alm.entity.TestCase;

import java.util.List;

public interface TestCaseService {

    TestCase createTestCase(TestCase testCase);

    TestCase updateTestCase(Long id, TestCase testCase);

    void deleteTestCase(Long id);

    TestCase getTestCaseById(Long id);

    TestCase getTestCaseByCaseId(String caseId);

    List<TestCase> getAllTestCases();

    List<TestCase> getTestCasesByProjectId(String projectId);

    List<TestCase> getTestCasesByStatus(String status);

    List<TestCase> getTestCasesByReqId(String reqId);

    List<TestCase> getTestCasesByDesignId(String designId);

    List<TestCase> searchTestCases(String keyword);

    String generateCaseId();
}