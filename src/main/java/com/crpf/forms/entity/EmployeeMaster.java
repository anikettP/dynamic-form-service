package com.crpf.forms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_master")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeMaster extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, unique = true, length = 50)
    private String employeeId;

    @Column(name = "force_no", nullable = false, unique = true, length = 50)
    private String forceNo;

    @Column(name = "employee_name", nullable = false, length = 150)
    private String employeeName;

    @Column(length = 50)
    private String rank;

    @Column(length = 100)
    private String unit;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;
}
