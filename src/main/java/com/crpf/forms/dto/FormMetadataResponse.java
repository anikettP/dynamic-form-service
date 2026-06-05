package com.crpf.forms.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormMetadataResponse {
    private String formName;
    private String description;
    private List<FormFieldMetadataResponse> fields;
}
