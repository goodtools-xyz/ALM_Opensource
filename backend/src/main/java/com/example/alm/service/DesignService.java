package com.example.alm.service;

import com.example.alm.entity.Design;

import java.util.List;

public interface DesignService {

    Design createDesign(Design design);

    Design updateDesign(Long id, Design design);

    void deleteDesign(Long id);

    Design getDesignById(Long id);

    Design getDesignByDesignId(String designId);

    List<Design> getAllDesigns();

    List<Design> getDesignsByProjectId(String projectId);

    List<Design> getDesignsByStatus(String status);

    List<Design> getDesignsByReqId(String reqId);

    List<Design> searchDesigns(String keyword);

    String generateDesignId();
}