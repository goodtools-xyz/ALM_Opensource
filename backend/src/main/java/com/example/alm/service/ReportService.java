
package com.example.alm.service;

import com.example.alm.entity.ReportTemplate;
import com.example.alm.entity.ReportInstance;
import com.example.alm.entity.KanbanBoard;

import java.util.List;

public interface ReportService {
    List<ReportTemplate> getAllTemplates();
    ReportTemplate getTemplateById(String templateId);
    ReportTemplate createTemplate(ReportTemplate template);
    ReportTemplate updateTemplate(String templateId, ReportTemplate template);
    void deleteTemplate(String templateId);
    
    ReportInstance generateReport(String templateId, String parameters);
    List<ReportInstance> getReportsByTemplate(String templateId);
    ReportInstance getReportById(String instanceId);
    void deleteReport(String instanceId);
    
    List<KanbanBoard> getAllKanbanBoards();
    List<KanbanBoard> getBoardsByProject(String projectId);
    KanbanBoard getBoardById(String boardId);
    KanbanBoard createBoard(KanbanBoard board);
    KanbanBoard updateBoard(String boardId, KanbanBoard board);
    void deleteBoard(String boardId);
}
