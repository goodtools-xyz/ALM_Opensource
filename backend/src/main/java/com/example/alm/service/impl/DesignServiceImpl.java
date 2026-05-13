package com.example.alm.service.impl;

import com.example.alm.entity.Design;
import com.example.alm.repository.DesignRepository;
import com.example.alm.service.DesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesignServiceImpl implements DesignService {

    @Autowired
    private DesignRepository designRepository;

    @Override
    public Design createDesign(Design design) {
        if (design.getDesignId() == null || design.getDesignId().isEmpty()) {
            design.setDesignId(generateDesignId());
        }
        design.setCreatedAt(LocalDateTime.now());
        design.setUpdatedAt(LocalDateTime.now());
        return designRepository.save(design);
    }

    @Override
    public Design updateDesign(Long id, Design design) {
        Design existing = designRepository.findById(id).orElseThrow();
        existing.setTitle(design.getTitle());
        existing.setDescription(design.getDescription());
        existing.setType(design.getType());
        existing.setStatus(design.getStatus());
        existing.setVersion(design.getVersion());
        existing.setProjectId(design.getProjectId());
        existing.setModule(design.getModule());
        existing.setRelatedReqId(design.getRelatedReqId());
        existing.setDesigner(design.getDesigner());
        existing.setFilePath(design.getFilePath());
        existing.setUpdatedAt(LocalDateTime.now());
        return designRepository.save(existing);
    }

    @Override
    public void deleteDesign(Long id) {
        designRepository.deleteById(id);
    }

    @Override
    public Design getDesignById(Long id) {
        return designRepository.findById(id).orElse(null);
    }

    @Override
    public Design getDesignByDesignId(String designId) {
        return designRepository.findByDesignId(designId).orElse(null);
    }

    @Override
    public List<Design> getAllDesigns() {
        return designRepository.findAll();
    }

    @Override
    public List<Design> getDesignsByProjectId(String projectId) {
        return designRepository.findByProjectId(projectId);
    }

    @Override
    public List<Design> getDesignsByStatus(String status) {
        return designRepository.findByStatus(status);
    }

    @Override
    public List<Design> getDesignsByReqId(String reqId) {
        return designRepository.findByRelatedReqId(reqId);
    }

    @Override
    public List<Design> searchDesigns(String keyword) {
        return designRepository.findAll().stream()
                .filter(design -> design.getTitle().contains(keyword) ||
                        design.getDesignId().contains(keyword) ||
                        (design.getDescription() != null && design.getDescription().contains(keyword)))
                .collect(Collectors.toList());
    }

    @Override
    public String generateDesignId() {
        long count = designRepository.count() + 1;
        return String.format("DES-%04d", count);
    }
}