
package com.example.alm.service;

import com.example.alm.entity.ApprovalFlow;
import com.example.alm.entity.ApprovalTask;
import com.example.alm.entity.ApprovalStep;

import java.util.List;
import java.util.Map;

public interface ApprovalService {
    List<ApprovalFlow> getAllFlows();
    ApprovalFlow getFlowById(String flowId);
    ApprovalFlow createFlow(ApprovalFlow flow);
    ApprovalFlow updateFlow(String flowId, ApprovalFlow flow);
    void deleteFlow(String flowId);
    
    List<ApprovalStep> getStepsByFlow(String flowId);
    ApprovalStep createStep(ApprovalStep step);
    ApprovalStep updateStep(String stepId, ApprovalStep step);
    void deleteStep(String stepId);
    
    ApprovalTask createTask(String flowId, String documentId, String documentType, String applicant);
    ApprovalTask getTaskById(String taskId);
    List<ApprovalTask> getTasksByStatus(String status);
    ApprovalTask approveTask(String taskId, String approver, String comment);
    ApprovalTask rejectTask(String taskId, String approver, String comment);
    ApprovalTask addAdHocApprover(String taskId, String approver);
    ApprovalTask delegateTask(String taskId, String delegateTo);
    ApprovalTask withdrawTask(String taskId);
}
