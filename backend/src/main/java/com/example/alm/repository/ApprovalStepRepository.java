
package com.example.alm.repository;

import com.example.alm.entity.ApprovalStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalStepRepository extends JpaRepository<ApprovalStep, String> {
    List<ApprovalStep> findByFlowIdOrderByStepOrder(String flowId);
}
