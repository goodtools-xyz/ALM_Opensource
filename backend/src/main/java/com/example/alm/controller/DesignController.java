package com.example.alm.controller;

import com.example.alm.entity.Design;
import com.example.alm.service.DesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/design")
@CrossOrigin(origins = "*")
public class DesignController {

    @Autowired
    private DesignService designService;

    @GetMapping
    public List<Design> getAllDesigns(
            @RequestParam(required = false) String projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String reqId,
            @RequestParam(required = false) String keyword) {
        if (projectId != null && !projectId.isEmpty()) {
            return designService.getDesignsByProjectId(projectId);
        }
        if (status != null && !status.isEmpty()) {
            return designService.getDesignsByStatus(status);
        }
        if (reqId != null && !reqId.isEmpty()) {
            return designService.getDesignsByReqId(reqId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return designService.searchDesigns(keyword);
        }
        return designService.getAllDesigns();
    }

    @GetMapping("/{id}")
    public Design getDesignById(@PathVariable Long id) {
        return designService.getDesignById(id);
    }

    @GetMapping("/design/{designId}")
    public Design getDesignByDesignId(@PathVariable String designId) {
        return designService.getDesignByDesignId(designId);
    }

    @PostMapping
    public Design createDesign(@RequestBody Design design) {
        return designService.createDesign(design);
    }

    @PutMapping("/{id}")
    public Design updateDesign(@PathVariable Long id, @RequestBody Design design) {
        return designService.updateDesign(id, design);
    }

    @DeleteMapping("/{id}")
    public void deleteDesign(@PathVariable Long id) {
        designService.deleteDesign(id);
    }

    @GetMapping("/generate-id")
    public String generateDesignId() {
        return designService.generateDesignId();
    }
}