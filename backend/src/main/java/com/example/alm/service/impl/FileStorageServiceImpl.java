
package com.example.alm.service.impl;

import com.example.alm.entity.FileStorage;
import com.example.alm.entity.FileFolder;
import com.example.alm.entity.FileAuditTrail;
import com.example.alm.repository.FileStorageRepository;
import com.example.alm.repository.FileFolderRepository;
import com.example.alm.repository.FileAuditTrailRepository;
import com.example.alm.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    
    @Autowired
    private FileFolderRepository folderRepository;
    
    @Autowired
    private FileStorageRepository fileRepository;
    
    @Autowired
    private FileAuditTrailRepository auditRepository;
    
    @Override
    public List<FileFolder> getAllFolders() {
        return folderRepository.findAll();
    }
    
    @Override
    public List<FileFolder> getChildFolders(String parentFolderId) {
        return folderRepository.findByParentFolderId(parentFolderId);
    }
    
    @Override
    public FileFolder getFolderById(String folderId) {
        return folderRepository.findById(folderId).orElse(null);
    }
    
    @Override
    public FileFolder createFolder(FileFolder folder) {
        folder.setFolderId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        folder.setCreatedAt(LocalDateTime.now());
        folder.setUpdatedAt(LocalDateTime.now());
        return folderRepository.save(folder);
    }
    
    @Override
    public FileFolder updateFolder(String folderId, FileFolder folder) {
        FileFolder existing = folderRepository.findById(folderId).orElse(null);
        if (existing != null) {
            existing.setName(folder.getName());
            existing.setDescription(folder.getDescription());
            existing.setUpdatedAt(LocalDateTime.now());
            return folderRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteFolder(String folderId) {
        folderRepository.deleteById(folderId);
    }
    
    @Override
    public List<FileStorage> getAllFiles() {
        return fileRepository.findAll();
    }
    
    @Override
    public List<FileStorage> getFilesByFolder(String folderId) {
        return fileRepository.findByFolderId(folderId);
    }
    
    @Override
    public FileStorage getFileById(String fileId) {
        return fileRepository.findById(fileId).orElse(null);
    }
    
    @Override
    public FileStorage createFile(FileStorage file) {
        file.setFileId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        file.setVersion("1.0");
        file.setControlled("Y");
        file.setStatus("ACTIVE");
        file.setCreatedAt(LocalDateTime.now());
        file.setUpdatedAt(LocalDateTime.now());
        
        FileStorage saved = fileRepository.save(file);
        
        addAuditTrail(file.getFileId(), "CREATE", file.getCreatedBy(), "", "", "File created");
        
        return saved;
    }
    
    @Override
    public FileStorage updateFile(String fileId, FileStorage file) {
        FileStorage existing = fileRepository.findById(fileId).orElse(null);
        if (existing != null) {
            addAuditTrail(fileId, "UPDATE", file.getCreatedBy(), "", "", "File updated");
            
            existing.setName(file.getName());
            existing.setDescription(file.getDescription());
            existing.setStatus(file.getStatus());
            existing.setVersion(file.getVersion());
            existing.setUpdatedAt(LocalDateTime.now());
            
            return fileRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteFile(String fileId) {
        FileStorage file = fileRepository.findById(fileId).orElse(null);
        if (file != null) {
            addAuditTrail(fileId, "DELETE", file.getCreatedBy(), "", "", "File deleted");
            fileRepository.deleteById(fileId);
        }
    }
    
    @Override
    public void archiveFile(String fileId) {
        FileStorage file = fileRepository.findById(fileId).orElse(null);
        if (file != null) {
            file.setStatus("ARCHIVED");
            file.setArchivedAt(LocalDateTime.now());
            addAuditTrail(fileId, "ARCHIVE", file.getCreatedBy(), "", "", "File archived");
            fileRepository.save(file);
        }
    }
    
    @Override
    public List<FileAuditTrail> getFileAuditTrail(String fileId) {
        return auditRepository.findByFileIdOrderByCreatedAtDesc(fileId);
    }
    
    @Override
    public void addAuditTrail(String fileId, String action, String userId, String userName, String ipAddress, String details) {
        FileAuditTrail audit = new FileAuditTrail();
        audit.setAuditId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        audit.setFileId(fileId);
        audit.setAction(action);
        audit.setUserId(userId);
        audit.setUserName(userName);
        audit.setIpAddress(ipAddress);
        audit.setDetails(details);
        audit.setCreatedAt(LocalDateTime.now());
        auditRepository.save(audit);
    }
    
    @Override
    public FileStorage uploadFile(MultipartFile file, String folderId, String createdBy) {
        try {
            String uploadDir = "uploads/files/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : "";
            String newFilename = UUID.randomUUID().toString().replace("-", "") + fileExtension;
            
            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath);
            
            FileStorage fileStorage = new FileStorage();
            fileStorage.setFileId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
            fileStorage.setName(originalFilename);
            fileStorage.setPath(filePath.toString());
            fileStorage.setFolderId(folderId);
            fileStorage.setFileSize(file.getSize());
            fileStorage.setFileType(file.getContentType());
            fileStorage.setVersion("1.0");
            fileStorage.setControlled("Y");
            fileStorage.setStatus("ACTIVE");
            fileStorage.setCreatedBy(createdBy);
            fileStorage.setCreatedAt(LocalDateTime.now());
            fileStorage.setUpdatedAt(LocalDateTime.now());
            
            FileStorage saved = fileRepository.save(fileStorage);
            
            addAuditTrail(saved.getFileId(), "UPLOAD", createdBy, "", "", "File uploaded: " + originalFilename);
            
            return saved;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<FileStorage> uploadFiles(MultipartFile[] files, String folderId, String createdBy) {
        List<FileStorage> uploadedFiles = new java.util.ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                uploadedFiles.add(uploadFile(file, folderId, createdBy));
            }
        }
        return uploadedFiles;
    }
}
