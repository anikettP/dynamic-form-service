package com.crpf.forms.repository;

import com.crpf.forms.entity.EmployeeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeMasterRepository extends JpaRepository<EmployeeMaster, Long> {
    List<EmployeeMaster> findAllByActiveTrue();
    Optional<EmployeeMaster> findByIdAndActiveTrue(Long id);
    Optional<EmployeeMaster> findByEmployeeId(String employeeId);
    Optional<EmployeeMaster> findByForceNo(String forceNo);
    Optional<EmployeeMaster> findByEmployeeIdAndActiveTrue(String employeeId);
    Optional<EmployeeMaster> findByForceNoAndActiveTrue(String forceNo);
    boolean existsByEmployeeId(String employeeId);
    boolean existsByForceNo(String forceNo);
    boolean existsByEmployeeIdAndActiveTrue(String employeeId);
    boolean existsByForceNoAndActiveTrue(String forceNo);
    boolean existsByEmployeeIdAndIdNotAndActiveTrue(String employeeId, Long id);
    boolean existsByForceNoAndIdNotAndActiveTrue(String forceNo, Long id);
}
