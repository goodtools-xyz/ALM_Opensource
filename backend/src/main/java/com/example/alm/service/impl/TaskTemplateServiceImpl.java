
package com.example.alm.service.impl;

import com.example.alm.entity.TaskTemplate;
import com.example.alm.entity.TaskTemplateField;
import com.example.alm.repository.TaskTemplateRepository;
import com.example.alm.repository.TaskTemplateFieldRepository;
import com.example.alm.service.TaskTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskTemplateServiceImpl implements TaskTemplateService {
    
    @Autowired
    private TaskTemplateRepository templateRepository;
    
    @Autowired
    private TaskTemplateFieldRepository fieldRepository;
    
    @Override
    public List<TaskTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }
    
    @Override
    public TaskTemplate getTemplateById(String templateId) {
        return templateRepository.findById(templateId).orElse(null);
    }
    
    @Override
    public List<TaskTemplate> getTemplatesByCategory(String category) {
        return templateRepository.findByCategory(category);
    }
    
    @Override
    public TaskTemplate createTemplate(TaskTemplate template) {
        template.setTemplateId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        template.setStatus("ACTIVE");
        return templateRepository.save(template);
    }
    
    @Override
    public TaskTemplate updateTemplate(String templateId, TaskTemplate template) {
        TaskTemplate existing = templateRepository.findById(templateId).orElse(null);
        if (existing != null) {
            existing.setName(template.getName());
            existing.setCategory(template.getCategory());
            existing.setDescription(template.getDescription());
            existing.setProjectType(template.getProjectType());
            existing.setConfig(template.getConfig());
            existing.setStatus(template.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            return templateRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteTemplate(String templateId) {
        fieldRepository.deleteByTemplateId(templateId);
        templateRepository.deleteById(templateId);
    }
    
    @Override
    public List<TaskTemplateField> getFieldsByTemplate(String templateId) {
        return fieldRepository.findByTemplateIdOrderByFieldOrder(templateId);
    }
    
    @Override
    public TaskTemplateField createField(TaskTemplateField field) {
        field.setFieldId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        return fieldRepository.save(field);
    }
    
    @Override
    public TaskTemplateField updateField(String fieldId, TaskTemplateField field) {
        TaskTemplateField existing = fieldRepository.findById(fieldId).orElse(null);
        if (existing != null) {
            existing.setFieldName(field.getFieldName());
            existing.setFieldLabel(field.getFieldLabel());
            existing.setFieldType(field.getFieldType());
            existing.setRequired(field.getRequired());
            existing.setDefaultValue(field.getDefaultValue());
            existing.setOptions(field.getOptions());
            existing.setFieldOrder(field.getFieldOrder());
            return fieldRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteField(String fieldId) {
        fieldRepository.deleteById(fieldId);
    }
}
