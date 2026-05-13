package com.example.alm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraceNodeDTO {
    private String id;
    private String name;
    private String type;
    private String status;
    private String description;
}