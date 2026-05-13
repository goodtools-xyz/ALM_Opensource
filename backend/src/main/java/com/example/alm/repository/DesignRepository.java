package com.example.alm.repository;

import com.example.alm.entity.Design;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DesignRepository extends JpaRepository<Design, Long> {

    Optional<Design> findByDesignId(String designId);

    List<Design> findByProjectId(String projectId);

    List<Design> findByStatus(String status);

    List<Design> findByRelatedReqId(String reqId);

    List<Design> findByType(String type);

    List<Design> findByTitleContaining(String keyword);

    boolean existsByDesignId(String designId);
}