
package com.example.alm.service.impl;

import com.example.alm.entity.KnowledgeSpace;
import com.example.alm.entity.KnowledgePage;
import com.example.alm.entity.KnowledgePageVersion;
import com.example.alm.repository.KnowledgeSpaceRepository;
import com.example.alm.repository.KnowledgePageRepository;
import com.example.alm.repository.KnowledgePageVersionRepository;
import com.example.alm.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class KnowledgeServiceImpl implements KnowledgeService {
    
    @Autowired
    private KnowledgeSpaceRepository spaceRepository;
    
    @Autowired
    private KnowledgePageRepository pageRepository;
    
    @Autowired
    private KnowledgePageVersionRepository versionRepository;
    
    @Override
    public List<KnowledgeSpace> getAllSpaces() {
        return spaceRepository.findAll();
    }
    
    @Override
    public KnowledgeSpace getSpaceById(String spaceId) {
        return spaceRepository.findById(spaceId).orElse(null);
    }
    
    @Override
    public KnowledgeSpace createSpace(KnowledgeSpace space) {
        space.setSpaceId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        space.setCreatedAt(LocalDateTime.now());
        space.setUpdatedAt(LocalDateTime.now());
        space.setStatus("ACTIVE");
        return spaceRepository.save(space);
    }
    
    @Override
    public KnowledgeSpace updateSpace(String spaceId, KnowledgeSpace space) {
        KnowledgeSpace existing = spaceRepository.findById(spaceId).orElse(null);
        if (existing != null) {
            existing.setName(space.getName());
            existing.setSpaceKey(space.getSpaceKey());
            existing.setDescription(space.getDescription());
            existing.setStatus(space.getStatus());
            existing.setUpdatedAt(LocalDateTime.now());
            return spaceRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deleteSpace(String spaceId) {
        spaceRepository.deleteById(spaceId);
    }
    
    @Override
    public List<KnowledgePage> getPagesBySpace(String spaceId) {
        return pageRepository.findBySpaceId(spaceId);
    }
    
    @Override
    public List<KnowledgePage> getChildPages(String parentPageId) {
        return pageRepository.findByParentPageId(parentPageId);
    }
    
    @Override
    public KnowledgePage getPageById(String pageId) {
        return pageRepository.findById(pageId).orElse(null);
    }
    
    @Override
    public KnowledgePage createPage(KnowledgePage page) {
        page.setPageId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        page.setVersion("1.0");
        page.setCreatedAt(LocalDateTime.now());
        page.setUpdatedAt(LocalDateTime.now());
        page.setStatus("DRAFT");
        
        KnowledgePage saved = pageRepository.save(page);
        
        savePageVersion(saved, "Initial version");
        
        return saved;
    }
    
    @Override
    public KnowledgePage updatePage(String pageId, KnowledgePage page) {
        KnowledgePage existing = pageRepository.findById(pageId).orElse(null);
        if (existing != null) {
            savePageVersion(existing, "Auto-saved before update");
            
            existing.setTitle(page.getTitle());
            existing.setContent(page.getContent());
            existing.setContentType(page.getContentType());
            existing.setStatus(page.getStatus());
            existing.setUpdatedBy(page.getUpdatedBy());
            existing.setUpdatedAt(LocalDateTime.now());
            
            String[] versionParts = existing.getVersion().split("\\.");
            int minor = Integer.parseInt(versionParts[1]) + 1;
            existing.setVersion(versionParts[0] + "." + minor);
            
            return pageRepository.save(existing);
        }
        return null;
    }
    
    @Override
    public void deletePage(String pageId) {
        pageRepository.deleteById(pageId);
    }
    
    @Override
    public List<KnowledgePageVersion> getPageVersions(String pageId) {
        return versionRepository.findByPageIdOrderByCreatedAtDesc(pageId);
    }
    
    @Override
    public KnowledgePageVersion getPageVersion(String pageId, String version) {
        List<KnowledgePageVersion> versions = versionRepository.findByPageIdOrderByCreatedAtDesc(pageId);
        return versions.stream()
                .filter(v -> v.getVersion().equals(version))
                .findFirst()
                .orElse(null);
    }
    
    private void savePageVersion(KnowledgePage page, String changeLog) {
        KnowledgePageVersion version = new KnowledgePageVersion();
        version.setVersionId(UUID.randomUUID().toString().replace("-", "").substring(0, 32));
        version.setPageId(page.getPageId());
        version.setVersion(page.getVersion());
        version.setContent(page.getContent());
        version.setChangeLog(changeLog);
        version.setCreatedBy(page.getUpdatedBy());
        version.setCreatedAt(LocalDateTime.now());
        versionRepository.save(version);
    }
}
