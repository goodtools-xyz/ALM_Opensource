
package com.example.alm.service;

import com.example.alm.entity.KnowledgeSpace;
import com.example.alm.entity.KnowledgePage;
import com.example.alm.entity.KnowledgePageVersion;

import java.util.List;

public interface KnowledgeService {
    List<KnowledgeSpace> getAllSpaces();
    KnowledgeSpace getSpaceById(String spaceId);
    KnowledgeSpace createSpace(KnowledgeSpace space);
    KnowledgeSpace updateSpace(String spaceId, KnowledgeSpace space);
    void deleteSpace(String spaceId);
    
    List<KnowledgePage> getPagesBySpace(String spaceId);
    List<KnowledgePage> getChildPages(String parentPageId);
    KnowledgePage getPageById(String pageId);
    KnowledgePage createPage(KnowledgePage page);
    KnowledgePage updatePage(String pageId, KnowledgePage page);
    void deletePage(String pageId);
    
    List<KnowledgePageVersion> getPageVersions(String pageId);
    KnowledgePageVersion getPageVersion(String pageId, String version);
}
