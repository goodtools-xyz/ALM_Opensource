
package com.example.alm.repository;

import com.example.alm.entity.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileStorageRepository extends JpaRepository<FileStorage, String> {
    List<FileStorage> findByFolderId(String folderId);
    List<FileStorage> findByStatus(String status);
    List<FileStorage> findByControlled(String controlled);
    FileStorage findByIsoFileNumber(String isoFileNumber);
}
