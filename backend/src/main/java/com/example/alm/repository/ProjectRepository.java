package com.example.alm.repository;

import com.example.alm.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    
    Optional<Project> findByCode(String code);
    
    Optional<Project> findByName(String name);
    
    List<Project> findByStatus(String status);
    
    boolean existsByCode(String code);
}
