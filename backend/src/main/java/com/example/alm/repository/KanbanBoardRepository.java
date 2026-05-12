
package com.example.alm.repository;

import com.example.alm.entity.KanbanBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KanbanBoardRepository extends JpaRepository<KanbanBoard, String> {
    List<KanbanBoard> findByProjectId(String projectId);
    List<KanbanBoard> findByCreatedBy(String createdBy);
    List<KanbanBoard> findByShared(String shared);
}
