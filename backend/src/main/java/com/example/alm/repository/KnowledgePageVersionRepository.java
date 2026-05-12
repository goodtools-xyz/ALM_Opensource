
package com.example.alm.repository;

import com.example.alm.entity.KnowledgePageVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgePageVersionRepository extends JpaRepository<KnowledgePageVersion, String> {
    List<KnowledgePageVersion> findByPageIdOrderByCreatedAtDesc(String pageId);
}
