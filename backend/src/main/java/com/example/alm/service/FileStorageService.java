
package com.example.alm.service;

import com.example.alm.entity.FileStorage;
import com.example.alm.entity.FileFolder;
import com.example.alm.entity.FileAuditTrail;

import java.util.List;

public interface FileStorageService {
    List<FileFolder> getAllFolders();
    List<FileFolder> getChildFolders(String parentFolderId);
    FileFolder getFolderById(String folderId);
    FileFolder createFolder(FileFolder folder);
    FileFolder updateFolder(String folderId, FileFolder folder);
    void deleteFolder(String folderId);
    
    List<FileStorage> getAllFiles();
    List<FileStorage> getFilesByFolder(String folderId);
    FileStorage getFileById(String fileId);
    FileStorage createFile(FileStorage file);
    FileStorage updateFile(String fileId, FileStorage file);
    void deleteFile(String fileId);
    void archiveFile(String fileId);
    
    List<FileAuditTrail> getFileAuditTrail(String fileId);
    void addAuditTrail(String fileId, String action, String userId, String userName, String ipAddress, String details);
}
