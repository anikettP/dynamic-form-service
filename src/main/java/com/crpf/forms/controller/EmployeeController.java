package com.crpf.forms.controller;

import com.crpf.forms.dto.AuthRequest;
import com.crpf.forms.dto.AuthResponse;
import com.crpf.forms.entity.EmployeeMasterEntity;
import com.crpf.forms.service.AuthService;
import com.crpf.forms.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@Tag(name = "Employee", description = "API endpoints for user details")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/search/{forceNo}")
    @Operation(summary = "Employee Details")
    public ResponseEntity<EmployeeMasterEntity> login(@PathVariable String forceNo) {
        EmployeeMasterEntity employeeMasterEntity = employeeService.serachEmployeeMasterEntity(forceNo);
        return ResponseEntity.ok(employeeMasterEntity);
    }
}
