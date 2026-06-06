package com.crpf.forms.service;

import com.crpf.forms.dto.EmployeeMasterRequest;
import com.crpf.forms.dto.EmployeeMasterResponse;
import com.crpf.forms.entity.EmployeeMaster;
import com.crpf.forms.exception.DuplicateRecordException;
import com.crpf.forms.exception.ResourceNotFoundException;
import com.crpf.forms.repository.EmployeeMasterRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeMasterService {

    private final EmployeeMasterRepository employeeMasterRepository;

    public EmployeeMasterService(EmployeeMasterRepository employeeMasterRepository) {
        this.employeeMasterRepository = employeeMasterRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeMasterResponse> getAllEmployees() {
        return employeeMasterRepository.findAllByActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeMasterResponse getEmployeeById(Long id) {
        EmployeeMaster employee = employeeMasterRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return mapToResponse(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeMasterResponse getEmployeeByEmployeeId(String employeeId) {
        EmployeeMaster employee = employeeMasterRepository.findByEmployeeIdAndActiveTrue(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with employee ID: " + employeeId));
        return mapToResponse(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeMasterResponse getEmployeeByForceNo(String forceNo) {
        EmployeeMaster employee = employeeMasterRepository.findByForceNoAndActiveTrue(forceNo)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with force number: " + forceNo));
        return mapToResponse(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeMasterResponse> searchEmployees(String forceNo) {
        return employeeMasterRepository.findByForceNoAndActiveTrue(forceNo).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EmployeeMasterResponse createEmployee(EmployeeMasterRequest request) {
        if (employeeMasterRepository.existsByEmployeeIdAndActiveTrue(request.getEmployeeId())) {
            throw new DuplicateRecordException("Employee with Employee ID " + request.getEmployeeId() + " already exists");
        }
        if (employeeMasterRepository.existsByForceNoAndActiveTrue(request.getForceNo())) {
            throw new DuplicateRecordException("Employee with Force No " + request.getForceNo() + " already exists");
        }

        EmployeeMaster employee = EmployeeMaster.builder()
                .employeeId(request.getEmployeeId())
                .forceNo(request.getForceNo())
                .employeeName(request.getEmployeeName())
                .rank(request.getRank())
                .unit(request.getUnit())
                .active(true)
                .build();

        EmployeeMaster saved = employeeMasterRepository.save(employee);
        return mapToResponse(saved);
    }

    public EmployeeMasterResponse updateEmployee(Long id, EmployeeMasterRequest request) {
        EmployeeMaster existing = employeeMasterRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        if (employeeMasterRepository.existsByEmployeeIdAndIdNotAndActiveTrue(request.getEmployeeId(), id)) {
            throw new DuplicateRecordException("Employee with Employee ID " + request.getEmployeeId() + " already exists");
        }
        if (employeeMasterRepository.existsByForceNoAndIdNotAndActiveTrue(request.getForceNo(), id)) {
            throw new DuplicateRecordException("Employee with Force No " + request.getForceNo() + " already exists");
        }

        existing.setEmployeeId(request.getEmployeeId());
        existing.setForceNo(request.getForceNo());
        existing.setEmployeeName(request.getEmployeeName());
        existing.setRank(request.getRank());
        existing.setUnit(request.getUnit());

        EmployeeMaster saved = employeeMasterRepository.save(existing);
        return mapToResponse(saved);
    }

    public void deleteEmployee(Long id) {
        EmployeeMaster existing = employeeMasterRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

        existing.setActive(false);
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        existing.setDeletedBy(currentUser);
        existing.setDeletedAt(OffsetDateTime.now());

        employeeMasterRepository.save(existing);
    }

    private EmployeeMasterResponse mapToResponse(EmployeeMaster employee) {
        return EmployeeMasterResponse.builder()
                .id(employee.getId())
                .employeeId(employee.getEmployeeId())
                .forceNo(employee.getForceNo())
                .employeeName(employee.getEmployeeName())
                .rank(employee.getRank())
                .unit(employee.getUnit())
                .active(employee.isActive())
                .build();
    }
}
