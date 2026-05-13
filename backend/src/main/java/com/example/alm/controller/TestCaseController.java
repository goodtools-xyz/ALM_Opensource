package com.example.alm.controller;

import com.example.alm.entity.TestCase;
import com.example.alm.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testcase")
@CrossOrigin(origins = "*")
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    @GetMapping
    public List<TestCase> getAllTestCases(
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String reqId,
            @RequestParam(required = false) String designId,
            @RequestParam(required = false) String keyword) {
        if (projectId != null && !projectId.isEmpty()) {
            return testCaseService.getTestCasesByProjectId(projectId);
        }
        if (status != null && !status.isEmpty()) {
            return testCaseService.getTestCasesByStatus(status);
        }
        if (reqId != null && !reqId.isEmpty()) {
            return testCaseService.getTestCasesByReqId(reqId);
        }
        if (designId != null && !designId.isEmpty()) {
            return testCaseService.getTestCasesByDesignId(designId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return testCaseService.searchTestCases(keyword);
        }
        return testCaseService.getAllTestCases();
    }

    @GetMapping("/{id}")
    public TestCase getTestCaseById(@PathVariable Long id) {
        return testCaseService.getTestCaseById(id);
    }

    @GetMapping("/case/{caseId}")
    public TestCase getTestCaseByCaseId(@PathVariable String caseId) {
        return testCaseService.getTestCaseByCaseId(caseId);
    }

    @PostMapping
    public TestCase createTestCase(@RequestBody TestCase testCase) {
        return testCaseService.createTestCase(testCase);
    }

    @PutMapping("/{id}")
    public TestCase updateTestCase(@PathVariable Long id, @RequestBody TestCase testCase) {
        return testCaseService.updateTestCase(id, testCase);
    }

    @DeleteMapping("/{id}")
    public void deleteTestCase(@PathVariable Long id) {
        testCaseService.deleteTestCase(id);
    }

    @GetMapping("/generate-id")
    public String generateCaseId() {
        return testCaseService.generateCaseId();
    }
}