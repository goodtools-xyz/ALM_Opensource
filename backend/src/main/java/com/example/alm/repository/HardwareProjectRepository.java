
package com.example.alm.repository;

import com.example.alm.entity.HardwareProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 硬件项目Repository
 */
@Repository
public interface HardwareProjectRepository extends JpaRepository<HardwareProject, String> {
    
    List<HardwareProject> findByStatus(String status);
}
