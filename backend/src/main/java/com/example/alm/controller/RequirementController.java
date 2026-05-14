package com.example.alm.controller;

import com.example.alm.entity.ImportRecord;
import com.example.alm.entity.Requirement;
import com.example.alm.service.DocumentParseService;
import com.example.alm.service.ImportRecordService;
import com.example.alm.service.RequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.*;

@RestController
@RequestMapping("/requirement")
@CrossOrigin(origins = "*")
public class RequirementController {

    @Autowired
    private RequirementService requirementService;

    @Autowired
    private DocumentParseService documentParseService;

    @Autowired
    private ImportRecordService importRecordService;

    private static final String UPLOAD_DIR = "uploads/imports/";

    /**
     * 初始化上传目录
     */
    private void initUploadDir() throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }

    @GetMapping
    public List<Requirement> getAllRequirements(
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword) {
        if (projectId != null && !projectId.isEmpty()) {
            return requirementService.getRequirementsByProjectId(projectId);
        }
        if (status != null && !status.isEmpty()) {
            return requirementService.getRequirementsByStatus(status);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return requirementService.searchRequirements(keyword);
        }
        return requirementService.getAllRequirements();
    }

    @GetMapping("/{id}")
    public Requirement getRequirementById(@PathVariable Long id) {
        return requirementService.getRequirementById(id);
    }

    @GetMapping("/req/{reqId}")
    public Requirement getRequirementByReqId(@PathVariable String reqId) {
        return requirementService.getRequirementByReqId(reqId);
    }

    @PostMapping
    public Requirement createRequirement(@RequestBody Requirement requirement) {
        return requirementService.createRequirement(requirement);
    }

    @PutMapping("/{id}")
    public Requirement updateRequirement(@PathVariable Long id, @RequestBody Requirement requirement) {
        return requirementService.updateRequirement(id, requirement);
    }

    @DeleteMapping("/{id}")
    public void deleteRequirement(@PathVariable Long id) {
        requirementService.deleteRequirement(id);
    }

    @GetMapping("/generate-id")
    public String generateReqId() {
        return requirementService.generateReqId();
    }

    /**
     * 导入文档解析需求（预览模式，不保存）
     * @param file Word或Excel文档
     * @return 解析出的需求列表
     */
    @PostMapping("/import/preview")
    public Map<String, Object> importDocumentPreview(@RequestParam("file") MultipartFile file) {
        List<Requirement> requirements = documentParseService.parseDocument(file);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("count", requirements.size());
        result.put("data", requirements);
        return result;
    }

    /**
     * 导入文档解析需求（保存模式）
     * 保留原始文档并建立追溯关系
     * @param file Word或Excel文档
     * @return 导入结果
     */
    @PostMapping("/import/save")
    public Map<String, Object> importDocumentSave(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        
        // 验证文件
        if (file == null || file.isEmpty()) {
            result.put("success", false);
            result.put("message", "请选择要导入的文件");
            return result;
        }
        
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            result.put("success", false);
            result.put("message", "文件名不能为空");
            return result;
        }
        
        // 验证文件格式
        if (!originalFileName.toLowerCase().endsWith(".docx") && !originalFileName.toLowerCase().endsWith(".xlsx")) {
            result.put("success", false);
            result.put("message", "不支持的文件格式，仅支持 .docx 和 .xlsx 格式");
            return result;
        }
        
        try {
            // 1. 创建导入记录
            String importId = importRecordService.generateImportId();
            System.out.println("开始导入文档，importId: " + importId);
            
            // 2. 保存原始文档（先保存，再解析）
            initUploadDir();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String storedFileName = importId + fileExtension;
            Path filePath = Paths.get(UPLOAD_DIR + storedFileName).toAbsolutePath().normalize();
            Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            System.out.println("原始文档保存成功: " + filePath);
            
            // 3. 解析文档（从保存的文件读取，避免输入流已被读取的问题）
            List<Requirement> requirements = documentParseService.parseDocument(filePath.toFile());
            System.out.println("文档解析完成，共解析出 " + requirements.size() + " 条需求");
            
            // 4. 保存需求并关联导入记录ID
            int successCount = 0;
            int failCount = 0;
            
            for (Requirement req : requirements) {
                try {
                    req.setImportId(importId);
                    requirementService.createRequirement(req);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    System.err.println("保存需求失败: " + e.getMessage());
                }
            }
            
            // 5. 保存导入记录
            ImportRecord importRecord = new ImportRecord();
            importRecord.setImportId(importId);
            importRecord.setFileName(originalFileName);
            importRecord.setFilePath(filePath.toString());
            importRecord.setFileType(file.getContentType());
            importRecord.setFileSize(file.getSize());
            importRecord.setImportCount(requirements.size());
            importRecord.setSuccessCount(successCount);
            importRecord.setFailCount(failCount);
            importRecord.setStatus(failCount > 0 ? "PARTIAL" : "COMPLETED");
            importRecordService.createImportRecord(importRecord);
            
            result.put("success", true);
            result.put("importId", importId);
            result.put("total", requirements.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("message", "导入成功，原始文档已保留");
            System.out.println("导入完成: " + successCount + " 成功, " + failCount + " 失败");
            
        } catch (IOException e) {
            System.err.println("保存原始文档失败: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "保存原始文档失败: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("参数错误: " + e.getMessage());
            result.put("success", false);
            result.put("message", e.getMessage());
        } catch (Exception e) {
            System.err.println("导入失败: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "导入失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 根据导入记录ID查询需求（追溯功能）
     * @param importId 导入记录ID
     * @return 该次导入的所有需求
     */
    @GetMapping("/import/{importId}")
    public Map<String, Object> getRequirementsByImportId(@PathVariable String importId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取导入记录信息
        ImportRecord importRecord = importRecordService.getImportRecordById(importId);
        if (importRecord == null) {
            result.put("success", false);
            result.put("message", "导入记录不存在");
            return result;
        }
        
        // 获取该次导入的所有需求
        List<Requirement> requirements = requirementService.getRequirementsByImportId(importId);
        
        result.put("success", true);
        result.put("importRecord", importRecord);
        result.put("requirements", requirements);
        return result;
    }

    /**
     * 获取所有导入记录
     * @return 导入记录列表
     */
    @GetMapping("/import/records")
    public Map<String, Object> getAllImportRecords() {
        Map<String, Object> result = new HashMap<>();
        List<ImportRecord> records = importRecordService.getAllImportRecords();
        result.put("success", true);
        result.put("data", records);
        return result;
    }

    /**
     * 下载原始导入文档
     * @param importId 导入记录ID
     * @return 文件流
     */
    @GetMapping("/import/{importId}/download")
    public ResponseEntity<Resource> downloadOriginalDocument(@PathVariable String importId) {
        ImportRecord importRecord = importRecordService.getImportRecordById(importId);
        
        if (importRecord == null) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            Path filePath = Paths.get(importRecord.getFilePath());
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }
            
            Resource resource = new FileSystemResource(filePath);
            
            String contentType = importRecord.getFileType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = Files.probeContentType(filePath);
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + importRecord.getFileName() + "\"")
                    .body(resource);
                    
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * 在线预览Word文档内容
     * @param importId 导入记录ID
     * @return Word文档内容的HTML格式和条目化属性信息
     */
    @GetMapping("/import/{importId}/preview")
    public Map<String, Object> previewDocument(@PathVariable String importId) {
        Map<String, Object> result = new HashMap<>();
        
        // 获取导入记录信息
        ImportRecord importRecord = importRecordService.getImportRecordById(importId);
        if (importRecord == null) {
            result.put("success", false);
            result.put("message", "导入记录不存在");
            return result;
        }
        
        try {
            Path filePath = Paths.get(importRecord.getFilePath());
            if (!Files.exists(filePath)) {
                result.put("success", false);
                result.put("message", "文档文件不存在");
                return result;
            }
            
            // 获取该次导入的所有需求（条目化属性信息）
            List<Requirement> requirements = requirementService.getRequirementsByImportId(importId);
            
            // 解析Word文档内容
            String documentContent = parseWordDocument(filePath.toString());
            
            result.put("success", true);
            result.put("importRecord", importRecord);
            result.put("documentContent", documentContent);
            result.put("requirements", requirements);
            result.put("requirementCount", requirements.size());
            
        } catch (IOException e) {
            result.put("success", false);
            result.put("message", "预览文档失败: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 解析Word文档为HTML格式
     * @param filePath 文件路径
     * @return HTML内容
     */
    private String parseWordDocument(String filePath) throws IOException {
        StringBuilder htmlContent = new StringBuilder();
        
        try (InputStream is = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(is)) {
            
            htmlContent.append("<div class=\"word-document\">");
            
            // 处理段落
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                if (text == null || text.trim().isEmpty()) {
                    continue;
                }
                
                // 根据段落样式确定HTML标签
                String style = paragraph.getStyle();
                String textAlignment = paragraph.getAlignment() != null ? 
                    paragraph.getAlignment().name() : "LEFT";
                
                // 标题样式
                if (style != null && style.startsWith("Heading")) {
                    int level = 1;
                    try {
                        level = Integer.parseInt(style.replace("Heading", ""));
                    } catch (NumberFormatException e) {
                        level = 1;
                    }
                    if (level > 6) level = 6;
                    htmlContent.append(String.format("<h%d style=\"text-align:%s;\">%s</h%d>", 
                        level, textAlignment.toLowerCase(), escapeHtml(text), level));
                } else {
                    // 普通段落
                    htmlContent.append(String.format("<p style=\"text-align:%s; margin: 0.5em 0;\">%s</p>", 
                        textAlignment.toLowerCase(), escapeHtml(text)));
                }
            }
            
            // 处理表格
            for (XWPFTable table : document.getTables()) {
                htmlContent.append("<table border=\"1\" cellpadding=\"5\" cellspacing=\"0\" style=\"border-collapse: collapse; margin: 0.5em 0;\">");
                
                for (XWPFTableRow row : table.getRows()) {
                    htmlContent.append("<tr>");
                    for (XWPFTableCell cell : row.getTableCells()) {
                        String cellText = cell.getText();
                        htmlContent.append(String.format("<td>%s</td>", escapeHtml(cellText != null ? cellText : "")));
                    }
                    htmlContent.append("</tr>");
                }
                
                htmlContent.append("</table>");
            }
            
            htmlContent.append("</div>");
        }
        
        return htmlContent.toString();
    }

    /**
     * HTML转义
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}