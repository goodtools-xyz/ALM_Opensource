
package com.example.alm.repository;

import com.example.alm.entity.HardwarePcb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PCB设计Repository
 */
@Repository
public interface HardwarePcbRepository extends JpaRepository<HardwarePcb, String> {
    
    List<HardwarePcb> findByProjectId(String projectId);
    List<HardwarePcb> findByProjectIdAndStatus(String projectId, String status);
}
