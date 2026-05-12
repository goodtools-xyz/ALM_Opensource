
package com.example.alm.repository;

import com.example.alm.entity.Structure2dDrawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 2D图纸Repository
 */
@Repository
public interface Structure2dDrawingRepository extends JpaRepository<Structure2dDrawing, String> {
    
    List<Structure2dDrawing> findByProjectId(String projectId);
    List<Structure2dDrawing> findByProjectIdAndStatus(String projectId, String status);
}
