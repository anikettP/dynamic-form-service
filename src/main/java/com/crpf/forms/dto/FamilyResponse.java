package com.crpf.forms.dto;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyResponse {
    private Long id;
    private String employeeId;
    private Map<String, Object> formData;
}
