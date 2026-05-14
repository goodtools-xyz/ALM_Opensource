package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 文档导入记录实体
 * 用于记录从Word/Excel文档导入需求的历史记录
 * 支持从条目化的数据追溯回原始文档
 */
@Entity
@Table(name = "import_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportRecord {

    @Id
    @Column(name = "import_id", length = 32)
    private String importId;

    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName;

    @Column(name = "file_path", length = 500, nullable = false)
    private String filePath;

    @Column(name = "file_type", length = 50)
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "import_count")
    private Integer importCount;

    @Column(name = "success_count")
    private Integer successCount;

    @Column(name = "fail_count")
    private Integer failCount;

    @Column(name = "created_by", length = 32)
    private String createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "status", length = 32)
    private String status;
}
