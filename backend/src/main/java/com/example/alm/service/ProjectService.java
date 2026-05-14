package com.example.alm.service;

import com.example.alm.entity.Project;
import com.example.alm.entity.FileFolder;

import java.util.List;

public interface ProjectService {
    
    Project createProject(Project project);
    
    Project getProjectById(String projectId);
    
    Project getProjectByCode(String code);
    
    List<Project> getAllProjects();
    
    Project updateProject(String projectId, Project project);
    
    void deleteProject(String projectId);
    
    FileFolder getProjectRootFolder(String projectId);
}
