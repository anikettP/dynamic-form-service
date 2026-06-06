package com.crpf.forms.repository;

import com.crpf.forms.entity.EmployeeMasterEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeMasterEntityRepository extends JpaRepository<EmployeeMasterEntity, Long> {
    EmployeeMasterEntity findByForceNoAndActiveIsTrue(String forceNo);
}