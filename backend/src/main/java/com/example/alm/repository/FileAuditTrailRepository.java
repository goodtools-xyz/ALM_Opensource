
package com.example.alm.repository;

import com.example.alm.entity.FileAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileAuditTrailRepository extends JpaRepository<FileAuditTrail, String> {
    List<FileAuditTrail> findByFileIdOrderByCreatedAtDesc(String fileId);
    List<FileAuditTrail> findByUserId(String userId);
}
