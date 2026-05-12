
package com.example.alm.repository;

import com.example.alm.entity.ApprovalTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalTaskRepository extends JpaRepository<ApprovalTask, String> {
    List<ApprovalTask> findByFlowId(String flowId);
    List<ApprovalTask> findByDocumentId(String documentId);
    List<ApprovalTask> findByStatus(String status);
    List<ApprovalTask> findByApplicant(String applicant);
}
