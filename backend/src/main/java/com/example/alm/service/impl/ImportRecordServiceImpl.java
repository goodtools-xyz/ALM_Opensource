package com.example.alm.service.impl;

import com.example.alm.entity.ImportRecord;
import com.example.alm.repository.ImportRecordRepository;
import com.example.alm.service.ImportRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 导入记录服务实现类
 */
@Service
public class ImportRecordServiceImpl implements ImportRecordService {

    @Autowired
    private ImportRecordRepository importRecordRepository;

    @Override
    public ImportRecord createImportRecord(ImportRecord record) {
        if (record.getImportId() == null || record.getImportId().isEmpty()) {
            record.setImportId(generateImportId());
        }
        if (record.getCreatedAt() == null) {
            record.setCreatedAt(LocalDateTime.now());
        }
        if (record.getStatus() == null || record.getStatus().isEmpty()) {
            record.setStatus("COMPLETED");
        }
        return importRecordRepository.save(record);
    }

    @Override
    public ImportRecord updateImportRecord(String importId, ImportRecord record) {
        ImportRecord existing = importRecordRepository.findById(importId).orElse(null);
        if (existing != null) {
            existing.setFileName(record.getFileName());
            existing.setFilePath(record.getFilePath());
            existing.setFileType(record.getFileType());
            existing.setFileSize(record.getFileSize());
            existing.setImportCount(record.getImportCount());
            existing.setSuccessCount(record.getSuccessCount());
            existing.setFailCount(record.getFailCount());
            existing.setStatus(record.getStatus());
            return importRecordRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteImportRecord(String importId) {
        importRecordRepository.deleteById(importId);
    }

    @Override
    public ImportRecord getImportRecordById(String importId) {
        return importRecordRepository.findById(importId).orElse(null);
    }

    @Override
    public List<ImportRecord> getAllImportRecords() {
        return importRecordRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<ImportRecord> getImportRecordsByFileName(String fileName) {
        return importRecordRepository.findByFileName(fileName);
    }

    @Override
    public List<ImportRecord> getImportRecordsByStatus(String status) {
        return importRecordRepository.findByStatus(status);
    }

    @Override
    public String generateImportId() {
        long count = importRecordRepository.count() + 1;
        return String.format("IMP-%04d", count);
    }
}
