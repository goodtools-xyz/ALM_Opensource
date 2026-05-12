
package com.example.alm.repository;

import com.example.alm.entity.HardwareBom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 硬件BOM Repository
 */
@Repository
public interface HardwareBomRepository extends JpaRepository<HardwareBom, String> {
    
    List<HardwareBom> findByProjectId(String projectId);
    List<HardwareBom> findByProjectIdAndStatus(String projectId, String status);
}
