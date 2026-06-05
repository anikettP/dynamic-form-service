package com.crpf.forms.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyMemberRequest {
    private String forceNo;
    private String memberName;
    private LocalDate dob;
    private String relationship;
    private String memberType;
    private boolean employed;
    private boolean handicapped;
    private BigDecimal disabilityPercentage;
    private boolean autisticMember;
    private String aadhaarNumber;
    private String panNumber;
    private String mobileNumber;
    private String remarks;
}
