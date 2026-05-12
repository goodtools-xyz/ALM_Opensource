
package com.example.alm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "task_template_field")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskTemplateField {
    
    @Id
    @Column(name = "field_id", length = 32)
    private String fieldId;
    
    @Column(name = "template_id", length = 32, nullable = false)
    private String templateId;
    
    @Column(name = "field_name", length = 100)
    private String fieldName;
    
    @Column(name = "field_label", length = 100)
    private String fieldLabel;
    
    @Column(name = "field_type", length = 50)
    private String fieldType;
    
    @Column(name = "required", length = 1)
    private String required;
    
    @Column(name = "default_value", length = 500)
    private String defaultValue;
    
    @Column(name = "options", length = 1000)
    private String options;
    
    @Column(name = "field_order")
    private Integer fieldOrder;
}
