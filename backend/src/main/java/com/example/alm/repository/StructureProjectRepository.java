
package com.example.alm.repository;

import com.example.alm.entity.StructureProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 结构项目Repository
 */
@Repository
public interface StructureProjectRepository extends JpaRepository<StructureProject, String> {
    
    List<StructureProject> findByStatus(String status);
}
