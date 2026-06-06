package com.crpf.forms.controller;

import com.crpf.forms.dto.EducationRecordRequest;
import com.crpf.forms.dto.EducationRecordResponse;
import com.crpf.forms.service.EducationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/education")
@Tag(name = "Education Module", description = "Endpoints for employee education records management")
@SecurityRequirement(name = "bearerAuth")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get list of all active education records")
    public ResponseEntity<List<EducationRecordResponse>> getAllRecords() {
        return ResponseEntity.ok(educationService.getAllEducationRecords());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get education record details by ID")
    public ResponseEntity<EducationRecordResponse> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(educationService.getEducationRecordById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    @Operation(summary = "Create a new education record")
    public ResponseEntity<EducationRecordResponse> createRecord(@RequestBody EducationRecordRequest request) {
        EducationRecordResponse response = educationService.createEducationRecord(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/dynamicForm/{forceNo}")
    @PreAuthorize("hasAuthority('CREATE')")
    @Operation(summary = "Create a new education record")
    public ResponseEntity<Boolean> dynamicFormCreateRecord(@PathVariable String forceNo,@RequestBody Map<String, Object> formData) {
        Boolean response = educationService.createEducationRecord(forceNo,formData);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    @Operation(summary = "Update an existing education record by ID")
    public ResponseEntity<EducationRecordResponse> updateRecord(@PathVariable Long id, @RequestBody EducationRecordRequest request) {
        EducationRecordResponse response = educationService.updateEducationRecord(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/dynamicForm/{id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    @Operation(summary = "Update an existing education record by ID")
    public ResponseEntity<Boolean> dynamicFormUpdateRecord(@PathVariable Long id, @RequestBody Map<String, Object> formData) {
        Boolean response = educationService.updateEducationRecord(id, formData);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    @Operation(summary = "Soft delete an education record by ID (ADMIN only)")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        educationService.deleteEducationRecord(id);
        return ResponseEntity.noContent().build();
    }
}
