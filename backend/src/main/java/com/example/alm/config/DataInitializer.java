package com.example.alm.config;

import com.example.alm.entity.FileFolder;
import com.example.alm.repository.FileFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private FileFolderRepository folderRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initProjectLibraryFolders();
    }
    
    private void initProjectLibraryFolders() {
        if (folderRepository.count() > 0) {
            return;
        }
        
        Map<String, FileFolder> folderMap = new HashMap<>();
        
        String[] level1Dirs = {
            "项目管理",
            "客户需求管理",
            "产品设计和开发",
            "过程设计和开发",
            "项目质量",
            "供应商管理",
            "物流管理"
        };
        
        Map<String, String[]> level2Dirs = Map.of(
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
        
        for (String level1Dir : level1Dirs) {
            FileFolder level1Folder = createFolder(null, level1Dir, "/" + level1Dir);
            folderMap.put(level1Dir, level1Folder);
        }
        
        for (Map.Entry<String, String[]> entry : level2Dirs.entrySet()) {
            FileFolder parentFolder = folderMap.get(entry.getKey());
            if (parentFolder != null) {
                for (String level2Dir : entry.getValue()) {
                    createFolder(parentFolder.getFolderId(), level2Dir, parentFolder.getPath() + "/" + level2Dir);
                }
            }
        }
        
        System.out.println("项目资料库目录结构初始化完成！");
    }
    
    private FileFolder createFolder(String parentFolderId, String name, String path) {
        FileFolder folder = new FileFolder();
        folder.setFolderId(generateFolderId());
        folder.setParentFolderId(parentFolderId);
        folder.setName(name);
        folder.setPath(path);
        folder.setDescription("A-SPICE 过程域目录");
        folder.setCreatedBy("system");
        folder.setCreatedAt(LocalDateTime.now());
        folder.setUpdatedAt(LocalDateTime.now());
        return folderRepository.save(folder);
    }
    
    private String generateFolderId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
}
