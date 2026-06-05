package com.crpf.forms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "education_record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationRecord extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "force_no", nullable = false, length = 50)
    private String forceNo;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 50)
    private String rank;

    @Column(length = 100)
    private String unit;

    @Column(name = "education_type", nullable = false, length = 100)
    private String educationType;

    @Column(name = "school_name", length = 150)
    private String schoolName;

    @Column(name = "passing_year")
    private LocalDate passingYear;

    @Column(name = "exam_passed", length = 100)
    private String examPassed;

    @Column(length = 100)
    private String subject1;

    @Column(length = 100)
    private String subject2;

    @Column(length = 100)
    private String subject3;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(length = 250)
    private String remarks;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
