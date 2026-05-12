
package com.example.alm.repository;

import com.example.alm.entity.ApprovalFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalFlowRepository extends JpaRepository<ApprovalFlow, String> {
    List<ApprovalFlow> findByType(String type);
    List<ApprovalFlow> findByStatus(String status);
}
