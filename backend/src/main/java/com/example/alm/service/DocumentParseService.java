package com.example.alm.service;

import com.example.alm.entity.Requirement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档解析服务接口
 * 支持从Word/Excel文档中提取需求条目
 */
public interface DocumentParseService {

    /**
     * 解析Word文档（.docx格式）
     * @param file Word文档文件
     * @return 解析出的需求列表
     */
    List<Requirement> parseWordDocument(MultipartFile file);

    /**
     * 解析Excel文档（.xlsx格式）
     * @param file Excel文档文件
     * @return 解析出的需求列表
     */
    List<Requirement> parseExcelDocument(MultipartFile file);

    /**
     * 根据文件类型自动选择解析方式
     * @param file 文档文件
     * @return 解析出的需求列表
     */
    List<Requirement> parseDocument(MultipartFile file);
}