
package com.example.alm.controller;

import com.example.alm.entity.FileStorage;
import com.example.alm.entity.FileFolder;
import com.example.alm.entity.FileAuditTrail;
import com.example.alm.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/storage")
public class FileStorageController {
    
    @Autowired
    private FileStorageService storageService;
    
    @GetMapping("/folders")
    public ResponseEntity<List<FileFolder>> getAllFolders() {
        return ResponseEntity.ok(storageService.getAllFolders());
    }
    
    @GetMapping("/folders/{folderId}")
    public ResponseEntity<FileFolder> getFolderById(@PathVariable String folderId) {
        FileFolder folder = storageService.getFolderById(folderId);
        return folder != null ? ResponseEntity.ok(folder) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/folders/{folderId}/children")
    public ResponseEntity<List<FileFolder>> getChildFolders(@PathVariable String folderId) {
        return ResponseEntity.ok(storageService.getChildFolders(folderId));
    }
    
    @PostMapping("/folders")
    public ResponseEntity<FileFolder> createFolder(@RequestBody FileFolder folder) {
        return ResponseEntity.ok(storageService.createFolder(folder));
    }
    
    @PutMapping("/folders/{folderId}")
    public ResponseEntity<FileFolder> updateFolder(@PathVariable String folderId, @RequestBody FileFolder folder) {
        FileFolder updated = storageService.updateFolder(folderId, folder);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/folders/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable String folderId) {
        storageService.deleteFolder(folderId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/files")
    public ResponseEntity<List<FileStorage>> getAllFiles() {
        return ResponseEntity.ok(storageService.getAllFiles());
    }
    
    @GetMapping("/files/{fileId}")
    public ResponseEntity<FileStorage> getFileById(@PathVariable String fileId) {
        FileStorage file = storageService.getFileById(fileId);
        return file != null ? ResponseEntity.ok(file) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/folders/{folderId}/files")
    public ResponseEntity<List<FileStorage>> getFilesByFolder(@PathVariable String folderId) {
        return ResponseEntity.ok(storageService.getFilesByFolder(folderId));
    }
    
    @PostMapping("/files")
    public ResponseEntity<FileStorage> createFile(@RequestBody FileStorage file) {
        return ResponseEntity.ok(storageService.createFile(file));
    }
    
    @PostMapping("/files/upload")
    public ResponseEntity<FileStorage> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("folderId") String folderId,
            @RequestParam(value = "createdBy", defaultValue = "system") String createdBy) {
        FileStorage uploadedFile = storageService.uploadFile(file, folderId, createdBy);
        return ResponseEntity.ok(uploadedFile);
    }
    
    @PostMapping("/files/uploads")
    public ResponseEntity<List<FileStorage>> uploadFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("folderId") String folderId,
            @RequestParam(value = "createdBy", defaultValue = "system") String createdBy) {
        List<FileStorage> uploadedFiles = storageService.uploadFiles(files, folderId, createdBy);
        return ResponseEntity.ok(uploadedFiles);
    }
    
    @PutMapping("/files/{fileId}")
    public ResponseEntity<FileStorage> updateFile(@PathVariable String fileId, @RequestBody FileStorage file) {
        FileStorage updated = storageService.updateFile(fileId, file);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileId) {
        storageService.deleteFile(fileId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/files/{fileId}/archive")
    public ResponseEntity<Void> archiveFile(@PathVariable String fileId) {
        storageService.archiveFile(fileId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/files/{fileId}/audit")
    public ResponseEntity<List<FileAuditTrail>> getFileAuditTrail(@PathVariable String fileId) {
        return ResponseEntity.ok(storageService.getFileAuditTrail(fileId));
    }
}
