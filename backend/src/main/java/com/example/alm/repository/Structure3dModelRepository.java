
package com.example.alm.repository;

import com.example.alm.entity.Structure3dModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 3D模型Repository
 */
@Repository
public interface Structure3dModelRepository extends JpaRepository<Structure3dModel, String> {
    
    List<Structure3dModel> findByProjectId(String projectId);
    List<Structure3dModel> findByProjectIdAndStatus(String projectId, String status);
}
