
package com.example.alm.controller;

import com.example.alm.entity.TaskTemplate;
import com.example.alm.entity.TaskTemplateField;
import com.example.alm.service.TaskTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/template")
public class TaskTemplateController {
    
    @Autowired
    private TaskTemplateService templateService;
    
    @GetMapping("/templates")
    public ResponseEntity<List<TaskTemplate>> getAllTemplates() {
        return ResponseEntity.ok(templateService.getAllTemplates());
    }
    
    @GetMapping("/templates/{templateId}")
    public ResponseEntity<TaskTemplate> getTemplateById(@PathVariable String templateId) {
        TaskTemplate template = templateService.getTemplateById(templateId);
        return template != null ? ResponseEntity.ok(template) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/templates/category/{category}")
    public ResponseEntity<List<TaskTemplate>> getTemplatesByCategory(@PathVariable String category) {
        return ResponseEntity.ok(templateService.getTemplatesByCategory(category));
    }
    
    @PostMapping("/templates")
    public ResponseEntity<TaskTemplate> createTemplate(@RequestBody TaskTemplate template) {
        return ResponseEntity.ok(templateService.createTemplate(template));
    }
    
    @PutMapping("/templates/{templateId}")
    public ResponseEntity<TaskTemplate> updateTemplate(@PathVariable String templateId, @RequestBody TaskTemplate template) {
        TaskTemplate updated = templateService.updateTemplate(templateId, template);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/templates/{templateId}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String templateId) {
        templateService.deleteTemplate(templateId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/templates/{templateId}/fields")
    public ResponseEntity<List<TaskTemplateField>> getFieldsByTemplate(@PathVariable String templateId) {
        return ResponseEntity.ok(templateService.getFieldsByTemplate(templateId));
    }
    
    @PostMapping("/fields")
    public ResponseEntity<TaskTemplateField> createField(@RequestBody TaskTemplateField field) {
        return ResponseEntity.ok(templateService.createField(field));
    }
    
    @PutMapping("/fields/{fieldId}")
    public ResponseEntity<TaskTemplateField> updateField(@PathVariable String fieldId, @RequestBody TaskTemplateField field) {
        TaskTemplateField updated = templateService.updateField(fieldId, field);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/fields/{fieldId}")
    public ResponseEntity<Void> deleteField(@PathVariable String fieldId) {
        templateService.deleteField(fieldId);
        return ResponseEntity.ok().build();
    }
}
