
package com.example.alm.controller;

import com.example.alm.entity.ApprovalFlow;
import com.example.alm.entity.ApprovalTask;
import com.example.alm.entity.ApprovalStep;
import com.example.alm.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/approval")
public class ApprovalController {
    
    @Autowired
    private ApprovalService approvalService;
    
    @GetMapping("/flows")
    public ResponseEntity<List<ApprovalFlow>> getAllFlows() {
        return ResponseEntity.ok(approvalService.getAllFlows());
    }
    
    @GetMapping("/flows/{flowId}")
    public ResponseEntity<ApprovalFlow> getFlowById(@PathVariable String flowId) {
        ApprovalFlow flow = approvalService.getFlowById(flowId);
        return flow != null ? ResponseEntity.ok(flow) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/flows")
    public ResponseEntity<ApprovalFlow> createFlow(@RequestBody ApprovalFlow flow) {
        return ResponseEntity.ok(approvalService.createFlow(flow));
    }
    
    @PutMapping("/flows/{flowId}")
    public ResponseEntity<ApprovalFlow> updateFlow(@PathVariable String flowId, @RequestBody ApprovalFlow flow) {
        ApprovalFlow updated = approvalService.updateFlow(flowId, flow);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/flows/{flowId}")
    public ResponseEntity<Void> deleteFlow(@PathVariable String flowId) {
        approvalService.deleteFlow(flowId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/flows/{flowId}/steps")
    public ResponseEntity<List<ApprovalStep>> getStepsByFlow(@PathVariable String flowId) {
        return ResponseEntity.ok(approvalService.getStepsByFlow(flowId));
    }
    
    @PostMapping("/steps")
    public ResponseEntity<ApprovalStep> createStep(@RequestBody ApprovalStep step) {
        return ResponseEntity.ok(approvalService.createStep(step));
    }
    
    @PutMapping("/steps/{stepId}")
    public ResponseEntity<ApprovalStep> updateStep(@PathVariable String stepId, @RequestBody ApprovalStep step) {
        ApprovalStep updated = approvalService.updateStep(stepId, step);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/steps/{stepId}")
    public ResponseEntity<Void> deleteStep(@PathVariable String stepId) {
        approvalService.deleteStep(stepId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/tasks")
    public ResponseEntity<ApprovalTask> createTask(@RequestBody Map<String, String> params) {
        ApprovalTask task = approvalService.createTask(
            params.get("flowId"),
            params.get("documentId"),
            params.get("documentType"),
            params.get("applicant")
        );
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }
    
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<ApprovalTask> getTaskById(@PathVariable String taskId) {
        ApprovalTask task = approvalService.getTaskById(taskId);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/tasks")
    public ResponseEntity<List<ApprovalTask>> getTasksByStatus(@RequestParam String status) {
        return ResponseEntity.ok(approvalService.getTasksByStatus(status));
    }
    
    @PostMapping("/tasks/{taskId}/approve")
    public ResponseEntity<ApprovalTask> approveTask(@PathVariable String taskId, @RequestBody Map<String, String> params) {
        ApprovalTask task = approvalService.approveTask(taskId, params.get("approver"), params.get("comment"));
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }
    
    @PostMapping("/tasks/{taskId}/reject")
    public ResponseEntity<ApprovalTask> rejectTask(@PathVariable String taskId, @RequestBody Map<String, String> params) {
        ApprovalTask task = approvalService.rejectTask(taskId, params.get("approver"), params.get("comment"));
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }
    
    @PostMapping("/tasks/{taskId}/adhoc")
    public ResponseEntity<ApprovalTask> addAdHocApprover(@PathVariable String taskId, @RequestBody Map<String, String> params) {
        ApprovalTask task = approvalService.addAdHocApprover(taskId, params.get("approver"));
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }
    
    @PostMapping("/tasks/{taskId}/delegate")
    public ResponseEntity<ApprovalTask> delegateTask(@PathVariable String taskId, @RequestBody Map<String, String> params) {
        ApprovalTask task = approvalService.delegateTask(taskId, params.get("delegateTo"));
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }
    
    @PostMapping("/tasks/{taskId}/withdraw")
    public ResponseEntity<ApprovalTask> withdrawTask(@PathVariable String taskId) {
        ApprovalTask task = approvalService.withdrawTask(taskId);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.badRequest().build();
    }
}
