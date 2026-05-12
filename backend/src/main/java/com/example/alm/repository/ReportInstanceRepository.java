
package com.example.alm.repository;

import com.example.alm.entity.ReportInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportInstanceRepository extends JpaRepository<ReportInstance, String> {
    List<ReportInstance> findByTemplateId(String templateId);
    List<ReportInstance> findByCreatedBy(String createdBy);
    List<ReportInstance> findByStatus(String status);
}
