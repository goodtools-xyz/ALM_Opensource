package com.example.alm.repository;

import com.example.alm.entity.ImportRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 导入记录数据访问层
 */
@Repository
public interface ImportRecordRepository extends JpaRepository<ImportRecord, String> {

    /**
     * 根据文件名查询导入记录
     */
    List<ImportRecord> findByFileName(String fileName);

    /**
     * 根据状态查询导入记录
     */
    List<ImportRecord> findByStatus(String status);

    /**
     * 根据创建人查询导入记录
     */
    List<ImportRecord> findByCreatedBy(String createdBy);

    /**
     * 按创建时间倒序查询所有记录
     */
    List<ImportRecord> findAllByOrderByCreatedAtDesc();
}
