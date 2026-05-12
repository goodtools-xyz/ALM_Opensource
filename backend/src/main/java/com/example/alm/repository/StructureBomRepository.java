
package com.example.alm.repository;

import com.example.alm.entity.StructureBom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 结构BOM Repository
 */
@Repository
public interface StructureBomRepository extends JpaRepository<StructureBom, String> {
    
    List<StructureBom> findByProjectId(String projectId);
    List<StructureBom> findByProjectIdAndStatus(String projectId, String status);
}
