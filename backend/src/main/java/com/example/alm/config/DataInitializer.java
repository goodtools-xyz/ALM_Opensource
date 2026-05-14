package com.example.alm.config;

import com.example.alm.entity.FileFolder;
import com.example.alm.repository.FileFolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.UUID;

@Configuration
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private FileFolderRepository folderRepository;
    
    @Override
    public void run(String... args) throws Exception {
        initProjectLibraryRoot();
    }
    
    private void initProjectLibraryRoot() {
        if (folderRepository.count() > 0) {
            return;
        }
        
        FileFolder projectLibraryRoot = new FileFolder();
        projectLibraryRoot.setFolderId(generateFolderId());
        projectLibraryRoot.setParentFolderId(null);
        projectLibraryRoot.setName("项目资料库");
        projectLibraryRoot.setPath("/项目资料库");
        projectLibraryRoot.setDescription("项目资料库根目录 - 所有项目文件夹存放于此");
        projectLibraryRoot.setCreatedBy("system");
        projectLibraryRoot.setCreatedAt(LocalDateTime.now());
        projectLibraryRoot.setUpdatedAt(LocalDateTime.now());
        folderRepository.save(projectLibraryRoot);
        
        FileFolder productLibraryRoot = new FileFolder();
        productLibraryRoot.setFolderId(generateFolderId());
        productLibraryRoot.setParentFolderId(null);
        productLibraryRoot.setName("产品库");
        productLibraryRoot.setPath("/产品库");
        productLibraryRoot.setDescription("产品库根目录 - 所有产品文档存放于此");
        productLibraryRoot.setCreatedBy("system");
        productLibraryRoot.setCreatedAt(LocalDateTime.now());
        productLibraryRoot.setUpdatedAt(LocalDateTime.now());
        folderRepository.save(productLibraryRoot);
        
        System.out.println("项目资料库和产品库根目录初始化完成！");
    }
    
    private String generateFolderId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
}
