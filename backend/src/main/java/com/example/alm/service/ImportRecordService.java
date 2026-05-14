package com.example.alm.service;

import com.example.alm.entity.ImportRecord;

import java.util.List;

/**
 * 导入记录服务接口
 */
public interface ImportRecordService {

    /**
     * 创建导入记录
     */
    ImportRecord createImportRecord(ImportRecord record);

    /**
     * 更新导入记录
     */
    ImportRecord updateImportRecord(String importId, ImportRecord record);

    /**
     * 删除导入记录
     */
    void deleteImportRecord(String importId);

    /**
     * 根据ID获取导入记录
     */
    ImportRecord getImportRecordById(String importId);

    /**
     * 获取所有导入记录
     */
    List<ImportRecord> getAllImportRecords();

    /**
     * 根据文件名查询导入记录
     */
    List<ImportRecord> getImportRecordsByFileName(String fileName);

    /**
     * 根据状态查询导入记录
     */
    List<ImportRecord> getImportRecordsByStatus(String status);

    /**
     * 生成导入记录ID
     */
    String generateImportId();
}
