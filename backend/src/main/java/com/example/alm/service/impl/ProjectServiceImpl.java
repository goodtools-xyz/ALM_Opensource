package com.example.alm.service.impl;

import com.example.alm.entity.Project;
import com.example.alm.entity.FileFolder;
import com.example.alm.repository.ProjectRepository;
import com.example.alm.repository.FileFolderRepository;
import com.example.alm.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private FileFolderRepository folderRepository;
    
    private static final String[] LEVEL1_DIRS = {
        "项目管理",
        "客户需求管理",
        "产品设计和开发",
        "过程设计和开发",
        "项目质量",
        "供应商管理",
        "物流管理"
    };
    
    private static final Map<String, String[]> LEVEL2_DIRS = Map.of(
        "产品设计和开发", new String[]{
            "系统需求",
            "系统设计",
            "软件需求",
            "软件系统设计",
            "软件详细设计",
            "软件单元测试",
            "软件集成测试",
            "软件合格性测试",
            "系统集成测试",
            "系统合格性测试",
            "软件发布",
            "硬件设计",
            "结构设计",
            "光学设计",
            "机器学习",
            "外包供应商管理",
            "配置管理",
            "研发质量管理"
        }
    );
    
    @Override
    @Transactional
    public Project createProject(Project project) {
        String projectId = generateId();
        project.setProjectId(projectId);
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        project.setStatus("ACTIVE");
        
        if (project.getCode() == null || project.getCode().isEmpty()) {
            project.setCode(generateProjectCode(project.getName()));
        }
        
        Project savedProject = projectRepository.save(project);
        
        createProjectFolders(projectId, project.getName());
        
        return savedProject;
    }
    
    @Override
    public Project getProjectById(String projectId) {
        return projectRepository.findById(projectId).orElse(null);
    }
    
    @Override
    public Project getProjectByCode(String code) {
        return projectRepository.findByCode(code).orElse(null);
    }
    
    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    
    @Override
    @Transactional
    public Project updateProject(String projectId, Project project) {
        Project existing = projectRepository.findById(projectId).orElse(null);
        if (existing != null) {
            existing.setName(project.getName());
            existing.setDescription(project.getDescription());
            existing.setStatus(project.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            return projectRepository.save(existing);
        }
        return null;
    }
    
    @Override
    @Transactional
    public void deleteProject(String projectId) {
        FileFolder rootFolder = getProjectRootFolder(projectId);
        if (rootFolder != null) {
            deleteFolderRecursively(rootFolder.getFolderId());
        }
        projectRepository.deleteById(projectId);
    }
    
    @Override
    public FileFolder getProjectRootFolder(String projectId) {
        String rootPath = "/项目资料库/" + projectId;
        return folderRepository.findAll().stream()
            .filter(folder -> folder.getPath() != null && folder.getPath().equals(rootPath))
            .findFirst()
            .orElse(null);
    }
    
    private void createProjectFolders(String projectId, String projectName) {
        String projectRootPath = "/项目资料库/" + projectId;
        
        FileFolder projectLibraryRoot = folderRepository.findAll().stream()
            .filter(f -> f.getName().equals("项目资料库") && f.getParentFolderId() == null)
            .findFirst()
            .orElse(null);
        
        String parentFolderId = projectLibraryRoot != null ? projectLibraryRoot.getFolderId() : null;
        
        FileFolder rootFolder = new FileFolder();
        rootFolder.setFolderId(generateId());
        rootFolder.setParentFolderId(parentFolderId);
        rootFolder.setName(projectName);
        rootFolder.setPath(projectRootPath);
        rootFolder.setDescription("项目根目录: " + projectName);
        rootFolder.setCreatedBy("system");
        rootFolder.setCreatedAt(LocalDateTime.now());
        rootFolder.setUpdatedAt(LocalDateTime.now());
        FileFolder savedRoot = folderRepository.save(rootFolder);
        
        Map<String, FileFolder> folderMap = new HashMap<>();
        
        for (String level1Dir : LEVEL1_DIRS) {
            String level1Path = projectRootPath + "/" + level1Dir;
            FileFolder level1Folder = createFolder(savedRoot.getFolderId(), level1Dir, level1Path);
            folderMap.put(level1Dir, level1Folder);
        }
        
        for (Map.Entry<String, String[]> entry : LEVEL2_DIRS.entrySet()) {
            FileFolder parentFolder = folderMap.get(entry.getKey());
            if (parentFolder != null) {
                for (String level2Dir : entry.getValue()) {
                    String level2Path = parentFolder.getPath() + "/" + level2Dir;
                    createFolder(parentFolder.getFolderId(), level2Dir, level2Path);
                }
            }
        }
    }
    
    private FileFolder createFolder(String parentFolderId, String name, String path) {
        FileFolder folder = new FileFolder();
        folder.setFolderId(generateId());
        folder.setParentFolderId(parentFolderId);
        folder.setName(name);
        folder.setPath(path);
        folder.setDescription("A-SPICE 过程域目录");
        folder.setCreatedBy("system");
        folder.setCreatedAt(LocalDateTime.now());
        folder.setUpdatedAt(LocalDateTime.now());
        return folderRepository.save(folder);
    }
    
    private void deleteFolderRecursively(String folderId) {
        List<FileFolder> children = folderRepository.findAll().stream()
            .filter(f -> folderId.equals(f.getParentFolderId()))
            .toList();
        
        for (FileFolder child : children) {
            deleteFolderRecursively(child.getFolderId());
        }
        
        folderRepository.deleteById(folderId);
    }
    
    private String generateId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
    
    private String generateProjectCode(String name) {
        String code = name.replaceAll("[\\s\\-_]+", "").toUpperCase().substring(0, Math.min(10, name.length()));
        code += "-" + System.currentTimeMillis() % 10000;
        return code;
    }
}
