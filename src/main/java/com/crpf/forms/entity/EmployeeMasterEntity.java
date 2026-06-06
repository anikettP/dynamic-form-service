package com.crpf.forms.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "employee_master")
public class EmployeeMasterEntity {
    @Id
    @ColumnDefault("nextval('employee_master_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "force_no", nullable = false, length = 50)
    private String forceNo;

    @Size(max = 255)
    @NotNull
    @Column(name = "employee_name", nullable = false)
    private String employeeName;

    @Size(max = 100)
    @Column(name = "rank", length = 100)
    private String rank;

    @Size(max = 255)
    @Column(name = "unit")
    private String unit;

    @NotNull
    @ColumnDefault("true")
    @Column(name = "active", nullable = false)
    private Boolean active = false;

    @Size(max = 100)
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Size(max = 100)
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Size(max = 100)
    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Size(max = 50)
    @NotNull
    @Column(name = "employee_id", nullable = false, length = 50)
    private String employeeId;

}