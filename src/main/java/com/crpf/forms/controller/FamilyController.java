package com.crpf.forms.controller;

import com.crpf.forms.dto.FamilyMemberRequest;
import com.crpf.forms.dto.FamilyMemberResponse;
import com.crpf.forms.service.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/family")
@Tag(name = "Family Module", description = "Endpoints for dependent family members management")
@SecurityRequirement(name = "bearerAuth")
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get list of all active family members")
    public ResponseEntity<List<FamilyMemberResponse>> getAllMembers() {
        return ResponseEntity.ok(familyService.getAllFamilyMembers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get family member details by ID")
    public ResponseEntity<FamilyMemberResponse> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(familyService.getFamilyMemberById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE')")
    @Operation(summary = "Create a new family member record")
    public ResponseEntity<FamilyMemberResponse> createMember(@RequestBody FamilyMemberRequest request) {
        FamilyMemberResponse response = familyService.createFamilyMember(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE')")
    @Operation(summary = "Update an existing family member record by ID")
    public ResponseEntity<FamilyMemberResponse> updateMember(@PathVariable Long id, @RequestBody FamilyMemberRequest request) {
        FamilyMemberResponse response = familyService.updateFamilyMember(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    @Operation(summary = "Soft delete a family member record by ID (ADMIN only)")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        familyService.deleteFamilyMember(id);
        return ResponseEntity.noContent().build();
    }
}
