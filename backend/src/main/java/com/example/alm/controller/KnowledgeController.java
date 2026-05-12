
package com.example.alm.controller;

import com.example.alm.entity.KnowledgeSpace;
import com.example.alm.entity.KnowledgePage;
import com.example.alm.entity.KnowledgePageVersion;
import com.example.alm.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {
    
    @Autowired
    private KnowledgeService knowledgeService;
    
    @GetMapping("/spaces")
    public ResponseEntity<List<KnowledgeSpace>> getAllSpaces() {
        return ResponseEntity.ok(knowledgeService.getAllSpaces());
    }
    
    @GetMapping("/spaces/{spaceId}")
    public ResponseEntity<KnowledgeSpace> getSpaceById(@PathVariable String spaceId) {
        KnowledgeSpace space = knowledgeService.getSpaceById(spaceId);
        return space != null ? ResponseEntity.ok(space) : ResponseEntity.notFound().build();
    }
    
    @PostMapping("/spaces")
    public ResponseEntity<KnowledgeSpace> createSpace(@RequestBody KnowledgeSpace space) {
        return ResponseEntity.ok(knowledgeService.createSpace(space));
    }
    
    @PutMapping("/spaces/{spaceId}")
    public ResponseEntity<KnowledgeSpace> updateSpace(@PathVariable String spaceId, @RequestBody KnowledgeSpace space) {
        KnowledgeSpace updated = knowledgeService.updateSpace(spaceId, space);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/spaces/{spaceId}")
    public ResponseEntity<Void> deleteSpace(@PathVariable String spaceId) {
        knowledgeService.deleteSpace(spaceId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/spaces/{spaceId}/pages")
    public ResponseEntity<List<KnowledgePage>> getPagesBySpace(@PathVariable String spaceId) {
        return ResponseEntity.ok(knowledgeService.getPagesBySpace(spaceId));
    }
    
    @GetMapping("/pages/{pageId}")
    public ResponseEntity<KnowledgePage> getPageById(@PathVariable String pageId) {
        KnowledgePage page = knowledgeService.getPageById(pageId);
        return page != null ? ResponseEntity.ok(page) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/pages/{pageId}/children")
    public ResponseEntity<List<KnowledgePage>> getChildPages(@PathVariable String pageId) {
        return ResponseEntity.ok(knowledgeService.getChildPages(pageId));
    }
    
    @PostMapping("/pages")
    public ResponseEntity<KnowledgePage> createPage(@RequestBody KnowledgePage page) {
        return ResponseEntity.ok(knowledgeService.createPage(page));
    }
    
    @PutMapping("/pages/{pageId}")
    public ResponseEntity<KnowledgePage> updatePage(@PathVariable String pageId, @RequestBody KnowledgePage page) {
        KnowledgePage updated = knowledgeService.updatePage(pageId, page);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/pages/{pageId}")
    public ResponseEntity<Void> deletePage(@PathVariable String pageId) {
        knowledgeService.deletePage(pageId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/pages/{pageId}/versions")
    public ResponseEntity<List<KnowledgePageVersion>> getPageVersions(@PathVariable String pageId) {
        return ResponseEntity.ok(knowledgeService.getPageVersions(pageId));
    }
    
    @GetMapping("/pages/{pageId}/versions/{version}")
    public ResponseEntity<KnowledgePageVersion> getPageVersion(@PathVariable String pageId, @PathVariable String version) {
        KnowledgePageVersion pageVersion = knowledgeService.getPageVersion(pageId, version);
        return pageVersion != null ? ResponseEntity.ok(pageVersion) : ResponseEntity.notFound().build();
    }
}
