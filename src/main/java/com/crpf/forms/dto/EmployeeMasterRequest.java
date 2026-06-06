package com.crpf.forms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeMasterRequest {
    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    @NotBlank(message = "Force number is required")
    private String forceNo;

    @NotBlank(message = "Employee name is required")
    private String employeeName;

    private String rank;
    private String unit;
}
