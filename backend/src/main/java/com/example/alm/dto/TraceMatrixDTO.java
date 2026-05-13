package com.example.alm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraceMatrixDTO {
    private String reqId;
    private String reqName;
    private List<DesignItem> designs;
    private List<TestItem> tests;
    private List<DefectItem> defects;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DesignItem {
        private String designId;
        private String designName;
        private boolean traced;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TestItem {
        private String caseId;
        private String caseName;
        private boolean traced;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DefectItem {
        private String defectId;
        private String defectName;
        private boolean traced;
    }
}