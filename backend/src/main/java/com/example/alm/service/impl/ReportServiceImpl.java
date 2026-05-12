
package com.example.alm.service.impl;

import com.example.alm.entity.ReportTemplate;
import com.example.alm.entity.ReportInstance;
import com.example.alm.entity.KanbanBoard;
import com.example.alm.repository.ReportTemplateRepository;
import com.example.alm.repository.ReportInstanceRepository;
import com.example.alm.repository.KanbanBoardRepository;
import com.example.alm.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private ReportTemplateRepository templateRepository;
    
    @Autowired
    private ReportInstanceRepository instanceRepository;
    
    @Autowired
    private KanbanBoardRepository kanbanRepository;
    
    @Override
    public List<ReportTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }
    
    @Override
    public ReportTemplate getTemplateById(String templateId) {
        return templateRepository.findById(templateId).orElse(null);
    }
    
    @Override
    public ReportTemplate createTemplate(ReportTemplate template) {
        template.setTemplateId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        template.setStatus("ACTIVE");
        return templateRepository.save(template);
    }
    
    @Override
    public ReportTemplate updateTemplate(String templateId, ReportTemplate template) {
        ReportTemplate existing = templateRepository.findById(templateId).orElse(null);
        if (existing != null) {
            existing.setName(template.getName());
            existing.setDescription(template.getDescription());
            existing.setType(template.getType());
            existing.setConfig(template.getConfig());
            existing.setChartTypes(template.getChartTypes());
            existing.setStatus(template.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            return templateRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteTemplate(String templateId) {
        templateRepository.deleteById(templateId);
    }
    
    @Override
    public ReportInstance generateReport(String templateId, String parameters) {
        ReportTemplate template = templateRepository.findById(templateId).orElse(null);
        if (template == null) {
            return null;
        }
        
        ReportInstance instance = new ReportInstance();
        instance.setInstanceId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        instance.setTemplateId(templateId);
        instance.setName(template.getName() + " - " + LocalDateTime.now().toString());
        instance.setParameters(parameters);
        instance.setData("{}");
        instance.setStatus("COMPLETED");
        instance.setFormat("JSON");
        instance.setCreatedAt(LocalDateTime.now());
        instance.setGeneratedAt(LocalDateTime.now());
        
        return instanceRepository.save(instance);
    }
    
    @Override
    public List<ReportInstance> getReportsByTemplate(String templateId) {
        return instanceRepository.findByTemplateId(templateId);
    }
    
    @Override
    public ReportInstance getReportById(String instanceId) {
        return instanceRepository.findById(instanceId).orElse(null);
    }
    
    @Override
    public void deleteReport(String instanceId) {
        instanceRepository.deleteById(instanceId);
    }
    
    @Override
    public List<KanbanBoard> getAllKanbanBoards() {
        return kanbanRepository.findAll();
    }
    
    @Override
    public List<KanbanBoard> getBoardsByProject(String projectId) {
        return kanbanRepository.findByProjectId(projectId);
    }
    
    @Override
    public KanbanBoard getBoardById(String boardId) {
        return kanbanRepository.findById(boardId).orElse(null);
    }
    
    @Override
    public KanbanBoard createBoard(KanbanBoard board) {
        board.setBoardId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(LocalDateTime.now());
        board.setShared("N");
        return kanbanRepository.save(board);
    }
    
    @Override
    public KanbanBoard updateBoard(String boardId, KanbanBoard board) {
        KanbanBoard existing = kanbanRepository.findById(boardId).orElse(null);
        if (existing != null) {
            existing.setName(board.getName());
            existing.setDescription(board.getDescription());
            existing.setColumns(board.getColumns());
            existing.setCardFields(board.getCardFields());
            existing.setFilterConfig(board.getFilterConfig());
            existing.setProjectId(board.getProjectId());
            existing.setShared(board.getShared());
            existing.setUpdatedAt(LocalDateTime.now());
            return kanbanRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteBoard(String boardId) {
        kanbanRepository.deleteById(boardId);
    }
}
