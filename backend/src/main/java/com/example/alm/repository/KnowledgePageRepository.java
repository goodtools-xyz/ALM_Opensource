
package com.example.alm.repository;

import com.example.alm.entity.KnowledgePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KnowledgePageRepository extends JpaRepository<KnowledgePage, String> {
    List<KnowledgePage> findBySpaceId(String spaceId);
    List<KnowledgePage> findByParentPageId(String parentPageId);
    List<KnowledgePage> findBySpaceIdAndParentPageId(String spaceId, String parentPageId);
    List<KnowledgePage> findByTitleContaining(String title);
}
