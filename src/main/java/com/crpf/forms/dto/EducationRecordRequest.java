package com.crpf.forms.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationRecordRequest {
    private String forceNo;
    private String name;
    private String rank;
    private String unit;
    private String educationType;
    private String schoolName;
    private LocalDate passingYear;
    private String examPassed;
    private String subject1;
    private String subject2;
    private String subject3;
    private LocalDate startDate;
    private LocalDate endDate;
    private String remarks;
}
