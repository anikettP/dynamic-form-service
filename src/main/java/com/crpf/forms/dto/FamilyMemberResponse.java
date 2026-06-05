package com.crpf.forms.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyMemberResponse {
    private Long id;
    private String forceNo;
    private String memberName;
    private LocalDate dob;
    private Integer age;
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
    private boolean active;

    // Audit fields
    private String createdBy;
    private OffsetDateTime createdAt;
    private String updatedBy;
    private OffsetDateTime updatedAt;
}
