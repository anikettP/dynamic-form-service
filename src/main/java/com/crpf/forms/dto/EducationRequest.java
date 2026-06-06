package com.crpf.forms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationRequest {
    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    private Map<String, Object> formData;
}
