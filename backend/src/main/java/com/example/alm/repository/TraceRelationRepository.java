package com.example.alm.repository;

import com.example.alm.entity.TraceRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraceRelationRepository extends JpaRepository<TraceRelation, Long> {

    List<TraceRelation> findBySourceTypeAndSourceId(String sourceType, String sourceId);

    List<TraceRelation> findByTargetTypeAndTargetId(String targetType, String targetId);

    List<TraceRelation> findBySourceTypeAndSourceIdOrTargetTypeAndTargetId(
            String sourceType, String sourceId, String targetType, String targetId);

    List<TraceRelation> findByProjectId(String projectId);

    void deleteBySourceId(String sourceId);

    void deleteByTargetId(String targetId);
}