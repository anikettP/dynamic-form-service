package com.crpf.forms.controller;

import com.crpf.forms.dto.FormFieldMetadataResponse;
import com.crpf.forms.dto.FormMetadataResponse;
import com.crpf.forms.service.MetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@Tag(name = "Form Metadata", description = "Endpoints for fetching metadata-driven form schemas")
@SecurityRequirement(name = "bearerAuth")
public class MetadataController {

    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get list of all active forms")
    public ResponseEntity<List<FormMetadataResponse>> getAllForms() {
        return ResponseEntity.ok(metadataService.getAllForms());
    }

    @GetMapping("/{formName}")
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get schema details of a specific form by name")
    public ResponseEntity<FormMetadataResponse> getFormByName(@PathVariable String formName) {
        return ResponseEntity.ok(metadataService.getFormByName(formName));
    }

    @GetMapping("/{formName}/fields")
    @PreAuthorize("hasAuthority('VIEW')")
    @Operation(summary = "Get fields schema of a specific form by name")
    public ResponseEntity<List<FormFieldMetadataResponse>> getFormFields(@PathVariable String formName) {
        return ResponseEntity.ok(metadataService.getFormFields(formName));
    }
}
