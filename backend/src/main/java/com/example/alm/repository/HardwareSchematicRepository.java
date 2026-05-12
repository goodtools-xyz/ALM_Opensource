
package com.example.alm.repository;

import com.example.alm.entity.HardwareSchematic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 原理图Repository
 */
@Repository
public interface HardwareSchematicRepository extends JpaRepository<HardwareSchematic, String> {
    
    List<HardwareSchematic> findByProjectId(String projectId);
    List<HardwareSchematic> findByProjectIdAndStatus(String projectId, String status);
}
