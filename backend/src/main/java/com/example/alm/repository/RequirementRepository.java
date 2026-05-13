package com.example.alm.repository;

import com.example.alm.entity.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    Optional<Requirement> findByReqId(String reqId);

    List<Requirement> findByProjectId(String projectId);

    List<Requirement> findByStatus(String status);

    List<Requirement> findByPriority(String priority);

    List<Requirement> findByModule(String module);

    List<Requirement> findByType(String type);

    List<Requirement> findByTitleContaining(String keyword);

    boolean existsByReqId(String reqId);
}