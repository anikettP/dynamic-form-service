package com.crpf.forms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "family_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyMember extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "force_no", nullable = false, length = 50)
    private String forceNo;

    @Column(name = "member_name", nullable = false, length = 100)
    private String memberName;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "age")
    private Integer age;

    @Column(length = 50)
    private String relationship;

    @Column(name = "member_type", length = 50)
    private String memberType;

    @Column(nullable = false)
    @Builder.Default
    private boolean employed = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean handicapped = false;

    @Column(name = "disability_percentage", precision = 5, scale = 2)
    private BigDecimal disabilityPercentage;

    @Column(name = "autistic_member", nullable = false)
    @Builder.Default
    private boolean autisticMember = false;

    @Column(name = "aadhaar_number", length = 20)
    private String aadhaarNumber;

    @Column(name = "pan_number", length = 20)
    private String panNumber;

    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @Column(length = 255)
    private String remarks;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "form_data", columnDefinition = "jsonb")
    private Map<String, Object> formData;
}
