package com.crpf.forms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormFieldMetadataResponse {
    private String fieldName;
    private String label;
    private String fieldType;
    private boolean required;
    private boolean visible;
    private boolean editable;
    private Integer minLength;
    private Integer maxLength;
    private String regexPattern;
    private String dependsOnField;
    private String dependsOnValue;
}
