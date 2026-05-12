
package com.example.alm.service.impl;

import com.example.alm.entity.ApprovalFlow;
import com.example.alm.entity.ApprovalTask;
import com.example.alm.entity.ApprovalStep;
import com.example.alm.repository.ApprovalFlowRepository;
import com.example.alm.repository.ApprovalTaskRepository;
import com.example.alm.repository.ApprovalStepRepository;
import com.example.alm.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ApprovalServiceImpl implements ApprovalService {
    
    @Autowired
    private ApprovalFlowRepository flowRepository;
    
    @Autowired
    private ApprovalTaskRepository taskRepository;
    
    @Autowired
    private ApprovalStepRepository stepRepository;
    
    @Override
    public List<ApprovalFlow> getAllFlows() {
        return flowRepository.findAll();
    }
    
    @Override
    public ApprovalFlow getFlowById(String flowId) {
        return flowRepository.findById(flowId).orElse(null);
    }
    
    @Override
    public ApprovalFlow createFlow(ApprovalFlow flow) {
        flow.setFlowId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        flow.setCreatedAt(LocalDateTime.now());
        flow.setStatus("ACTIVE");
        return flowRepository.save(flow);
    }
    
    @Override
    public ApprovalFlow updateFlow(String flowId, ApprovalFlow flow) {
        ApprovalFlow existing = flowRepository.findById(flowId).orElse(null);
        if (existing != null) {
            existing.setName(flow.getName());
            existing.setDescription(flow.getDescription());
            existing.setType(flow.getType());
            existing.setConfig(flow.getConfig());
            existing.setStatus(flow.getStatus());
            return flowRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteFlow(String flowId) {
        flowRepository.deleteById(flowId);
    }
    
    @Override
    public List<ApprovalStep> getStepsByFlow(String flowId) {
        return stepRepository.findByFlowIdOrderByStepOrder(flowId);
    }
    
    @Override
    public ApprovalStep createStep(ApprovalStep step) {
        step.setStepId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        return stepRepository.save(step);
    }
    
    @Override
    public ApprovalStep updateStep(String stepId, ApprovalStep step) {
        ApprovalStep existing = stepRepository.findById(stepId).orElse(null);
        if (existing != null) {
            existing.setStepName(step.getStepName());
            existing.setStepOrder(step.getStepOrder());
            existing.setApprovers(step.getApprovers());
            existing.setType(step.getType());
            existing.setRequiredApprovalCount(step.getRequiredApprovalCount());
            return stepRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteStep(String stepId) {
        stepRepository.deleteById(stepId);
    }
    
    @Override
    public ApprovalTask createTask(String flowId, String documentId, String documentType, String applicant) {
        ApprovalTask task = new ApprovalTask();
        task.setTaskId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        task.setFlowId(flowId);
        task.setDocumentId(documentId);
        task.setDocumentType(documentType);
        task.setStatus("PENDING");
        task.setApplicant(applicant);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        
        List<ApprovalStep> steps = stepRepository.findByFlowIdOrderByStepOrder(flowId);
        if (!steps.isEmpty()) {
            task.setCurrentStep(steps.get(0).getStepId());
        }
        
        return taskRepository.save(task);
    }
    
    @Override
    public ApprovalTask getTaskById(String taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }
    
    @Override
    public List<ApprovalTask> getTasksByStatus(String status) {
        return taskRepository.findByStatus(status);
    }
    
    @Override
    public ApprovalTask approveTask(String taskId, String approver, String comment) {
        ApprovalTask task = taskRepository.findById(taskId).orElse(null);
        if (task != null && "PENDING".equals(task.getStatus())) {
            task.setStatus("APPROVED");
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }
        return null;
    }
    
    @Override
    public ApprovalTask rejectTask(String taskId, String approver, String comment) {
        ApprovalTask task = taskRepository.findById(taskId).orElse(null);
        if (task != null && "PENDING".equals(task.getStatus())) {
            task.setStatus("REJECTED");
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }
        return null;
    }
    
    @Override
    public ApprovalTask addAdHocApprover(String taskId, String approver) {
        ApprovalTask task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }
        return null;
    }
    
    @Override
    public ApprovalTask delegateTask(String taskId, String delegateTo) {
        ApprovalTask task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }
        return null;
    }
    
    @Override
    public ApprovalTask withdrawTask(String taskId) {
        ApprovalTask task = taskRepository.findById(taskId).orElse(null);
        if (task != null && "PENDING".equals(task.getStatus())) {
            task.setStatus("WITHDRAWN");
            task.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(task);
        }
        return null;
    }
}
