
package com.example.alm.service;

import com.example.alm.entity.TaskTemplate;
import com.example.alm.entity.TaskTemplateField;

import java.util.List;

public interface TaskTemplateService {
    List<TaskTemplate> getAllTemplates();
    TaskTemplate getTemplateById(String templateId);
    List<TaskTemplate> getTemplatesByCategory(String category);
    TaskTemplate createTemplate(TaskTemplate template);
    TaskTemplate updateTemplate(String templateId, TaskTemplate template);
    void deleteTemplate(String templateId);
    
    List<TaskTemplateField> getFieldsByTemplate(String templateId);
    TaskTemplateField createField(TaskTemplateField field);
    TaskTemplateField updateField(String fieldId, TaskTemplateField field);
    void deleteField(String fieldId);
}
