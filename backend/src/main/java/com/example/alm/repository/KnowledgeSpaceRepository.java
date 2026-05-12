
package com.example.alm.repository;

import com.example.alm.entity.KnowledgeSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KnowledgeSpaceRepository extends JpaRepository<KnowledgeSpace, String> {
    Optional<KnowledgeSpace> findBySpaceKey(String spaceKey);
    List<KnowledgeSpace> findByStatus(String status);
    List<KnowledgeSpace> findByNameContaining(String name);
}
