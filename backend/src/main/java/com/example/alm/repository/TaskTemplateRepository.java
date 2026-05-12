
package com.example.alm.repository;

import com.example.alm.entity.TaskTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTemplateRepository extends JpaRepository<TaskTemplate, String> {
    List<TaskTemplate> findByCategory(String category);
    List<TaskTemplate> findByProjectType(String projectType);
    List<TaskTemplate> findByStatus(String status);
}
