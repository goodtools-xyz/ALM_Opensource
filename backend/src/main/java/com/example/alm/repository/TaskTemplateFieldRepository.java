
package com.example.alm.repository;

import com.example.alm.entity.TaskTemplateField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskTemplateFieldRepository extends JpaRepository<TaskTemplateField, String> {
    List<TaskTemplateField> findByTemplateIdOrderByFieldOrder(String templateId);
    void deleteByTemplateId(String templateId);
}
