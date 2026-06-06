package com.crpf.forms.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeMasterResponse {
    private Long id;
    private String employeeId;
    private String forceNo;
    private String employeeName;
    private String rank;
    private String unit;
    private boolean active;
}
