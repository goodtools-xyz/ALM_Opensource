
package com.example.alm.repository;

import com.example.alm.entity.ReportTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, String> {
    List<ReportTemplate> findByType(String type);
    List<ReportTemplate> findByStatus(String status);
    List<ReportTemplate> findByNameContaining(String name);
}
