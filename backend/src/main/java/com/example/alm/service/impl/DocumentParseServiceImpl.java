package com.example.alm.service.impl;

import com.example.alm.entity.Requirement;
import com.example.alm.service.DocumentParseService;
import com.example.alm.service.RequirementService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文档解析服务实现
 * 支持从Word文档和Excel文档中提取需求条目
 */
@Service
public class DocumentParseServiceImpl implements DocumentParseService {

    @Autowired
    private RequirementService requirementService;

    // 需求标题匹配模式（支持带编号的标题，如：1. 需求标题、1.1 子需求、一、需求标题等）
    private static final Pattern TITLE_PATTERN = Pattern.compile(
            "^(\\d+[.、])+\\s*([^。，！？\\n]+)|^(一|二|三|四|五|六|七|八|九|十)+[、.．]\\s*([^。，！？\\n]+)"
    );

    // 优先级匹配模式
    private static final Pattern PRIORITY_PATTERN = Pattern.compile(
            "优先级[：:]\\s*(高|中|低|HIGH|MEDIUM|LOW)", Pattern.CASE_INSENSITIVE
    );

    // 类型匹配模式
    private static final Pattern TYPE_PATTERN = Pattern.compile(
            "类型[：:]\\s*(功能需求|非功能需求|接口需求|数据需求|FUNCTIONAL|NON_FUNCTIONAL|INTERFACE|DATA)", 
            Pattern.CASE_INSENSITIVE
    );

    @Override
    public List<Requirement> parseWordDocument(MultipartFile file) {
        List<Requirement> requirements = new ArrayList<>();
        
        try (InputStream inputStream = file.getInputStream();
             XWPFDocument document = new XWPFDocument(inputStream)) {
            
            StringBuilder fullText = new StringBuilder();
            
            // 读取所有段落
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                if (text != null && !text.trim().isEmpty()) {
                    fullText.append(text).append("\n");
                }
            }
            
            // 读取所有表格
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        String text = cell.getText();
                        if (text != null && !text.trim().isEmpty()) {
                            fullText.append(text).append("\t");
                        }
                    }
                    fullText.append("\n");
                }
            }
            
            requirements = parseTextToRequirements(fullText.toString());
            
        } catch (IOException e) {
            throw new RuntimeException("解析Word文档失败: " + e.getMessage(), e);
        }
        
        return requirements;
    }

    @Override
    public List<Requirement> parseExcelDocument(MultipartFile file) {
        List<Requirement> requirements = new ArrayList<>();
        
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            // 读取表头
            Row headerRow = sheet.getRow(0);
            int reqIdCol = -1, titleCol = -1, typeCol = -1, priorityCol = -1, 
                descriptionCol = -1, moduleCol = -1, ownerCol = -1;
            
            if (headerRow != null) {
                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    Cell cell = headerRow.getCell(i);
                    if (cell != null) {
                        String header = getCellValueAsString(cell).trim();
                        if (header.contains("需求ID") || header.contains("reqId")) {
                            reqIdCol = i;
                        } else if (header.contains("标题") || header.contains("名称")) {
                            titleCol = i;
                        } else if (header.contains("类型")) {
                            typeCol = i;
                        } else if (header.contains("优先级")) {
                            priorityCol = i;
                        } else if (header.contains("描述") || header.contains("说明")) {
                            descriptionCol = i;
                        } else if (header.contains("模块")) {
                            moduleCol = i;
                        } else if (header.contains("负责人") || header.contains("owner")) {
                            ownerCol = i;
                        }
                    }
                }
            }
            
            // 读取数据行
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;
                
                Requirement req = new Requirement();
                
                // 需求ID
                if (reqIdCol >= 0) {
                    Cell cell = row.getCell(reqIdCol);
                    if (cell != null) {
                        req.setReqId(getCellValueAsString(cell).trim());
                    }
                } else {
                    req.setReqId(requirementService.generateReqId());
                }
                
                // 标题（必需）
                if (titleCol >= 0) {
                    Cell cell = row.getCell(titleCol);
                    if (cell != null) {
                        String title = getCellValueAsString(cell).trim();
                        if (title.isEmpty()) continue;
                        req.setTitle(title);
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                
                // 类型
                if (typeCol >= 0) {
                    Cell cell = row.getCell(typeCol);
                    if (cell != null) {
                        String type = getCellValueAsString(cell).trim().toUpperCase();
                        if (!type.isEmpty()) {
                            req.setType(mapType(type));
                        }
                    }
                }
                
                // 优先级
                if (priorityCol >= 0) {
                    Cell cell = row.getCell(priorityCol);
                    if (cell != null) {
                        String priority = getCellValueAsString(cell).trim().toUpperCase();
                        if (!priority.isEmpty()) {
                            req.setPriority(mapPriority(priority));
                        }
                    }
                }
                
                // 描述
                if (descriptionCol >= 0) {
                    Cell cell = row.getCell(descriptionCol);
                    if (cell != null) {
                        req.setDescription(getCellValueAsString(cell).trim());
                    }
                }
                
                // 模块
                if (moduleCol >= 0) {
                    Cell cell = row.getCell(moduleCol);
                    if (cell != null) {
                        req.setModule(getCellValueAsString(cell).trim());
                    }
                }
                
                // 负责人
                if (ownerCol >= 0) {
                    Cell cell = row.getCell(ownerCol);
                    if (cell != null) {
                        req.setOwner(getCellValueAsString(cell).trim());
                    }
                }
                
                // 默认值
                if (req.getType() == null || req.getType().isEmpty()) {
                    req.setType("FUNCTIONAL");
                }
                if (req.getPriority() == null || req.getPriority().isEmpty()) {
                    req.setPriority("MEDIUM");
                }
                if (req.getStatus() == null || req.getStatus().isEmpty()) {
                    req.setStatus("PENDING");
                }
                
                requirements.add(req);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("解析Excel文档失败: " + e.getMessage(), e);
        }
        
        return requirements;
    }

    @Override
    public List<Requirement> parseDocument(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new IllegalArgumentException("文件名不能为空");
        }
        
        if (filename.toLowerCase().endsWith(".docx")) {
            return parseWordDocument(file);
        } else if (filename.toLowerCase().endsWith(".xlsx")) {
            return parseExcelDocument(file);
        } else {
            throw new IllegalArgumentException("不支持的文件格式，仅支持 .docx 和 .xlsx 格式");
        }
    }

    @Override
    public List<Requirement> parseDocument(File file) {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("文件不存在");
        }
        
        String filename = file.getName();
        
        if (filename.toLowerCase().endsWith(".docx")) {
            return parseWordDocument(file);
        } else if (filename.toLowerCase().endsWith(".xlsx")) {
            return parseExcelDocument(file);
        } else {
            throw new IllegalArgumentException("不支持的文件格式，仅支持 .docx 和 .xlsx 格式");
        }
    }

    /**
     * 解析Word文档（从File对象读取）
     */
    private List<Requirement> parseWordDocument(File file) {
        List<Requirement> requirements = new ArrayList<>();
        
        try (InputStream inputStream = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(inputStream)) {
            
            StringBuilder fullText = new StringBuilder();
            
            // 读取所有段落
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                if (text != null && !text.trim().isEmpty()) {
                    fullText.append(text).append("\n");
                }
            }
            
            // 读取所有表格
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        String text = cell.getText();
                        if (text != null && !text.trim().isEmpty()) {
                            fullText.append(text).append("\t");
                        }
                    }
                    fullText.append("\n");
                }
            }
            
            requirements = parseTextToRequirements(fullText.toString());
            
        } catch (IOException e) {
            throw new RuntimeException("解析Word文档失败: " + e.getMessage(), e);
        }
        
        return requirements;
    }

    /**
     * 解析Excel文档（从File对象读取）
     */
    private List<Requirement> parseExcelDocument(File file) {
        List<Requirement> requirements = new ArrayList<>();
        
        try (InputStream inputStream = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            // 读取表头
            Row headerRow = sheet.getRow(0);
            int reqIdCol = -1, titleCol = -1, typeCol = -1, priorityCol = -1, 
                descriptionCol = -1, moduleCol = -1, ownerCol = -1;
            
            if (headerRow != null) {
                for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                    Cell cell = headerRow.getCell(i);
                    if (cell != null) {
                        String header = getCellValueAsString(cell).trim();
                        if (header.contains("需求ID") || header.contains("reqId")) {
                            reqIdCol = i;
                        } else if (header.contains("标题") || header.contains("名称")) {
                            titleCol = i;
                        } else if (header.contains("类型")) {
                            typeCol = i;
                        } else if (header.contains("优先级")) {
                            priorityCol = i;
                        } else if (header.contains("描述") || header.contains("说明")) {
                            descriptionCol = i;
                        } else if (header.contains("模块")) {
                            moduleCol = i;
                        } else if (header.contains("负责人") || header.contains("owner")) {
                            ownerCol = i;
                        }
                    }
                }
            }
            
            // 读取数据行
            for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) continue;
                
                Requirement req = new Requirement();
                
                // 需求ID
                if (reqIdCol >= 0) {
                    Cell cell = row.getCell(reqIdCol);
                    if (cell != null) {
                        req.setReqId(getCellValueAsString(cell).trim());
                    }
                } else {
                    req.setReqId(requirementService.generateReqId());
                }
                
                // 标题（必需）
                if (titleCol >= 0) {
                    Cell cell = row.getCell(titleCol);
                    if (cell != null) {
                        String title = getCellValueAsString(cell).trim();
                        if (title.isEmpty()) continue;
                        req.setTitle(title);
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
                
                // 类型
                if (typeCol >= 0) {
                    Cell cell = row.getCell(typeCol);
                    if (cell != null) {
                        String type = getCellValueAsString(cell).trim().toUpperCase();
                        if (!type.isEmpty()) {
                            req.setType(mapType(type));
                        }
                    }
                }
                
                // 优先级
                if (priorityCol >= 0) {
                    Cell cell = row.getCell(priorityCol);
                    if (cell != null) {
                        String priority = getCellValueAsString(cell).trim().toUpperCase();
                        if (!priority.isEmpty()) {
                            req.setPriority(mapPriority(priority));
                        }
                    }
                }
                
                // 描述
                if (descriptionCol >= 0) {
                    Cell cell = row.getCell(descriptionCol);
                    if (cell != null) {
                        req.setDescription(getCellValueAsString(cell).trim());
                    }
                }
                
                // 模块
                if (moduleCol >= 0) {
                    Cell cell = row.getCell(moduleCol);
                    if (cell != null) {
                        req.setModule(getCellValueAsString(cell).trim());
                    }
                }
                
                // 负责人
                if (ownerCol >= 0) {
                    Cell cell = row.getCell(ownerCol);
                    if (cell != null) {
                        req.setOwner(getCellValueAsString(cell).trim());
                    }
                }
                
                // 默认值
                if (req.getType() == null || req.getType().isEmpty()) {
                    req.setType("FUNCTIONAL");
                }
                if (req.getPriority() == null || req.getPriority().isEmpty()) {
                    req.setPriority("MEDIUM");
                }
                if (req.getStatus() == null || req.getStatus().isEmpty()) {
                    req.setStatus("PENDING");
                }
                
                requirements.add(req);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("解析Excel文档失败: " + e.getMessage(), e);
        }
        
        return requirements;
    }

    /**
     * 从文本中解析需求条目
     */
    private List<Requirement> parseTextToRequirements(String text) {
        List<Requirement> requirements = new ArrayList<>();
        String[] lines = text.split("\\n");
        
        Requirement currentReq = null;
        StringBuilder description = new StringBuilder();
        
        // 获取当前数据库中的需求数量，用于生成唯一ID
        long baseCount = requirementService.getRequirementsCount();
        
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            
            // 尝试匹配需求标题
            Matcher titleMatcher = TITLE_PATTERN.matcher(line);
            if (titleMatcher.find()) {
                // 如果有当前需求，先保存
                if (currentReq != null) {
                    currentReq.setDescription(description.toString().trim());
                    requirements.add(currentReq);
                }
                
                // 创建新需求
                currentReq = new Requirement();
                String title = titleMatcher.group(2) != null ? titleMatcher.group(2) : titleMatcher.group(3);
                currentReq.setTitle(title.trim());
                // 使用递增计数器生成唯一ID，避免重复
                currentReq.setReqId(String.format("REQ-%04d", baseCount + requirements.size() + 1));
                currentReq.setType("FUNCTIONAL");
                currentReq.setPriority("MEDIUM");
                currentReq.setStatus("PENDING");
                description = new StringBuilder();
                
                // 检查行中是否包含优先级信息
                Matcher priorityMatcher = PRIORITY_PATTERN.matcher(line);
                if (priorityMatcher.find()) {
                    currentReq.setPriority(mapPriority(priorityMatcher.group(1)));
                }
                
                // 检查行中是否包含类型信息
                Matcher typeMatcher = TYPE_PATTERN.matcher(line);
                if (typeMatcher.find()) {
                    currentReq.setType(mapType(typeMatcher.group(1)));
                }
            } else if (currentReq != null) {
                // 将非标题行作为描述内容
                description.append(line).append("\n");
            }
        }
        
        // 保存最后一个需求
        if (currentReq != null) {
            currentReq.setDescription(description.toString().trim());
            requirements.add(currentReq);
        }
        
        return requirements;
    }

    /**
     * 获取单元格值作为字符串
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toString();
                }
                double numValue = cell.getNumericCellValue();
                if (numValue == Math.floor(numValue)) {
                    return String.valueOf((long) numValue);
                }
                return String.valueOf(numValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    return String.valueOf(cell.getNumericCellValue());
                }
            default:
                return "";
        }
    }

    /**
     * 映射优先级字符串
     */
    private String mapPriority(String priority) {
        priority = priority.toUpperCase();
        if (priority.contains("高") || priority.equals("HIGH")) {
            return "HIGH";
        } else if (priority.contains("中") || priority.equals("MEDIUM")) {
            return "MEDIUM";
        } else if (priority.contains("低") || priority.equals("LOW")) {
            return "LOW";
        }
        return "MEDIUM";
    }

    /**
     * 映射类型字符串
     */
    private String mapType(String type) {
        type = type.toUpperCase();
        if (type.contains("功能") || type.equals("FUNCTIONAL")) {
            return "FUNCTIONAL";
        } else if (type.contains("非功能") || type.equals("NON_FUNCTIONAL")) {
            return "NON_FUNCTIONAL";
        } else if (type.contains("接口") || type.equals("INTERFACE")) {
            return "INTERFACE";
        } else if (type.contains("数据") || type.equals("DATA")) {
            return "DATA";
        }
        return "FUNCTIONAL";
    }
}