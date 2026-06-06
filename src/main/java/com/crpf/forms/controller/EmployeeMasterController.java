package com.crpf.forms.controller;

import com.crpf.forms.dto.EmployeeMasterRequest;
import com.crpf.forms.dto.EmployeeMasterResponse;
import com.crpf.forms.service.EmployeeMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Master Module", description = "Endpoints for employee master details management")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeMasterController {

    private final EmployeeMasterService employeeMasterService;

    public EmployeeMasterController(EmployeeMasterService employeeMasterService) {
        this.employeeMasterService = employeeMasterService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get list of all active employees")
    public ResponseEntity<List<EmployeeMasterResponse>> getAllEmployees() {
        return ResponseEntity.ok(employeeMasterService.getAllEmployees());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get employee details by ID")
    public ResponseEntity<EmployeeMasterResponse> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeMasterService.getEmployeeById(id));
    }

    @GetMapping("/employee-id/{employeeId}")
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get employee details by Employee ID")
    public ResponseEntity<EmployeeMasterResponse> getEmployeeByEmployeeId(@PathVariable String employeeId) {
        return ResponseEntity.ok(employeeMasterService.getEmployeeByEmployeeId(employeeId));
    }

    @GetMapping("/force-no/{forceNo}")
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get employee details by Force Number")
    public ResponseEntity<EmployeeMasterResponse> getEmployeeByForceNo(@PathVariable String forceNo) {
        return ResponseEntity.ok(employeeMasterService.getEmployeeByForceNo(forceNo));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Search employee details by Force Number query parameter")
    public ResponseEntity<List<EmployeeMasterResponse>> searchEmployees(@RequestParam String forceNo) {
        return ResponseEntity.ok(employeeMasterService.searchEmployees(forceNo));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    @Operation(summary = "Create a new employee record")
    public ResponseEntity<EmployeeMasterResponse> createEmployee(@Valid @RequestBody EmployeeMasterRequest request) {
        EmployeeMasterResponse response = employeeMasterService.createEmployee(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    @Operation(summary = "Update an existing employee record by ID")
    public ResponseEntity<EmployeeMasterResponse> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeMasterRequest request) {
        EmployeeMasterResponse response = employeeMasterService.updateEmployee(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    @Operation(summary = "Soft delete an employee record by ID (ADMIN only)")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeMasterService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
