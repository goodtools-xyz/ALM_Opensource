
package com.example.alm.repository;

import com.example.alm.entity.FileFolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileFolderRepository extends JpaRepository<FileFolder, String> {
    List<FileFolder> findByParentFolderId(String parentFolderId);
    List<FileFolder> findByPathContaining(String path);
}
