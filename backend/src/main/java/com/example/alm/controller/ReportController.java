
package com.example.alm.controller;

import com.example.alm.entity.ReportTemplate;
import com.example.alm.entity.ReportInstance;
import com.example.alm.entity.KanbanBoard;
import com.example.alm.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    @GetMapping("/templates")
    public ResponseEntity<List<ReportTemplate>> getAllTemplates() {
        return ResponseEntity.ok(reportService.getAllTemplates());
    }
    
    @GetMapping("/templates/{templateId}")
    public ResponseEntity<ReportTemplate> getTemplateById(@PathVariable String templateId) {
        ReportTemplate template = reportService.getTemplateById(templateId);
        return template != null ? ResponseEntity.ok(template) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/templates")
    public ResponseEntity<ReportTemplate> createTemplate(@RequestBody ReportTemplate template) {
        return ResponseEntity.ok(reportService.createTemplate(template));
    }
    
    @PutMapping("/templates/{templateId}")
    public ResponseEntity<ReportTemplate> updateTemplate(@PathVariable String templateId, @RequestBody ReportTemplate template) {
        ReportTemplate updated = reportService.updateTemplate(templateId, template);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/templates/{templateId}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String templateId) {
        reportService.deleteTemplate(templateId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/generate/{templateId}")
    public ResponseEntity<ReportInstance> generateReport(@PathVariable String templateId, @RequestBody Map<String, Object> params) {
        String parameters = params.toString();
        ReportInstance report = reportService.generateReport(templateId, parameters);
        return report != null ? ResponseEntity.ok(report) : ResponseEntity.badRequest().build();
    }
    
    @GetMapping("/instances")
    public ResponseEntity<List<ReportInstance>> getReportsByTemplate(@RequestParam String templateId) {
        return ResponseEntity.ok(reportService.getReportsByTemplate(templateId));
    }
    
    @GetMapping("/instances/{instanceId}")
    public ResponseEntity<ReportInstance> getReportById(@PathVariable String instanceId) {
        ReportInstance report = reportService.getReportById(instanceId);
        return report != null ? ResponseEntity.ok(report) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/instances/{instanceId}")
    public ResponseEntity<Void> deleteReport(@PathVariable String instanceId) {
        reportService.deleteReport(instanceId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/kanban")
    public ResponseEntity<List<KanbanBoard>> getAllKanbanBoards() {
        return ResponseEntity.ok(reportService.getAllKanbanBoards());
    }
    
    @GetMapping("/kanban/project/{projectId}")
    public ResponseEntity<List<KanbanBoard>> getBoardsByProject(@PathVariable String projectId) {
        return ResponseEntity.ok(reportService.getBoardsByProject(projectId));
    }
    
    @GetMapping("/kanban/{boardId}")
    public ResponseEntity<KanbanBoard> getBoardById(@PathVariable String boardId) {
        KanbanBoard board = reportService.getBoardById(boardId);
        return board != null ? ResponseEntity.ok(board) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/kanban")
    public ResponseEntity<KanbanBoard> createBoard(@RequestBody KanbanBoard board) {
        return ResponseEntity.ok(reportService.createBoard(board));
    }
    
    @PutMapping("/kanban/{boardId}")
    public ResponseEntity<KanbanBoard> updateBoard(@PathVariable String boardId, @RequestBody KanbanBoard board) {
        KanbanBoard updated = reportService.updateBoard(boardId, board);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/kanban/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable String boardId) {
        reportService.deleteBoard(boardId);
        return ResponseEntity.ok().build();
    }
}
