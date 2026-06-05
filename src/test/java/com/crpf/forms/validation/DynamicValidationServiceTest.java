package com.crpf.forms.validation;

import com.crpf.forms.entity.FormFieldMetadata;
import com.crpf.forms.entity.FormMetadata;
import com.crpf.forms.exception.ValidationException;
import com.crpf.forms.repository.FormMetadataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class DynamicValidationServiceTest {

    private FormMetadataRepository formMetadataRepository;
    private DynamicValidationService validationService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        formMetadataRepository = Mockito.mock(FormMetadataRepository.class);
        objectMapper = new ObjectMapper();
        validationService = new DynamicValidationService(formMetadataRepository, objectMapper);
    }

    @Test
    void testValidateRequiredFieldSuccess() {
        FormMetadata form = new FormMetadata();
        form.setFormName("test-form");
        form.setFields(Collections.singletonList(
                FormFieldMetadata.builder()
                        .fieldName("username")
                        .label("Username")
                        .fieldType("TEXT")
                        .required(true)
                        .visible(true)
                        .editable(true)
                        .build()
        ));

        when(formMetadataRepository.findByFormNameIgnoreCaseAndActiveTrue(anyString()))
                .thenReturn(Optional.of(form));

        Map<String, Object> request = new HashMap<>();
        request.put("username", "aniket");

        assertDoesNotThrow(() -> validationService.validate("test-form", request));
    }

    @Test
    void testValidateRequiredFieldMissingThrowsException() {
        FormMetadata form = new FormMetadata();
        form.setFormName("test-form");
        form.setFields(Collections.singletonList(
                FormFieldMetadata.builder()
                        .fieldName("username")
                        .label("Username")
                        .fieldType("TEXT")
                        .required(true)
                        .build()
        ));

        when(formMetadataRepository.findByFormNameIgnoreCaseAndActiveTrue(anyString()))
                .thenReturn(Optional.of(form));

        Map<String, Object> request = new HashMap<>();
        // username is missing

        ValidationException exception = assertThrows(ValidationException.class,
                () -> validationService.validate("test-form", request));

        assertTrue(exception.getErrors().containsKey("username"));
        assertEquals("Username is required", exception.getErrors().get("username"));
    }

    @Test
    void testValidateLengthConstraints() {
        FormMetadata form = new FormMetadata();
        form.setFormName("test-form");
        form.setFields(Collections.singletonList(
                FormFieldMetadata.builder()
                        .fieldName("password")
                        .label("Password")
                        .fieldType("TEXT")
                        .required(true)
                        .minLength(6)
                        .maxLength(10)
                        .build()
        ));

        when(formMetadataRepository.findByFormNameIgnoreCaseAndActiveTrue(anyString()))
                .thenReturn(Optional.of(form));

        // Too short
        Map<String, Object> requestShort = new HashMap<>();
        requestShort.put("password", "123");
        ValidationException exShort = assertThrows(ValidationException.class,
                () -> validationService.validate("test-form", requestShort));
        assertTrue(exShort.getErrors().containsKey("password"));

        // Too long
        Map<String, Object> requestLong = new HashMap<>();
        requestLong.put("password", "1234567890123");
        ValidationException exLong = assertThrows(ValidationException.class,
                () -> validationService.validate("test-form", requestLong));
        assertTrue(exLong.getErrors().containsKey("password"));

        // Perfect length
        Map<String, Object> requestOk = new HashMap<>();
        requestOk.put("password", "1234567");
        assertDoesNotThrow(() -> validationService.validate("test-form", requestOk));
    }

    @Test
    void testValidateRegexConstraints() {
        FormMetadata form = new FormMetadata();
        form.setFormName("test-form");
        form.setFields(Collections.singletonList(
                FormFieldMetadata.builder()
                        .fieldName("aadhaar")
                        .label("Aadhaar Number")
                        .fieldType("TEXT")
                        .regexPattern("^\\d{12}$")
                        .build()
        ));

        when(formMetadataRepository.findByFormNameIgnoreCaseAndActiveTrue(anyString()))
                .thenReturn(Optional.of(form));

        // Invalid pattern
        Map<String, Object> requestInvalid = new HashMap<>();
        requestInvalid.put("aadhaar", "12345abc1234");
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validationService.validate("test-form", requestInvalid));
        assertTrue(ex.getErrors().containsKey("aadhaar"));

        // Valid pattern
        Map<String, Object> requestValid = new HashMap<>();
        requestValid.put("aadhaar", "123456789012");
        assertDoesNotThrow(() -> validationService.validate("test-form", requestValid));
    }

    @Test
    void testValidateConditionalDependency() {
        FormMetadata form = new FormMetadata();
        form.setFormName("test-form");
        form.setFields(Arrays.asList(
                FormFieldMetadata.builder()
                        .fieldName("handicapped")
                        .label("Handicapped")
                        .fieldType("BOOLEAN")
                        .build(),
                FormFieldMetadata.builder()
                        .fieldName("disability_percentage")
                        .label("Disability Percentage")
                        .fieldType("NUMBER")
                        .dependsOnField("handicapped")
                        .dependsOnValue("true")
                        .build()
        ));

        when(formMetadataRepository.findByFormNameIgnoreCaseAndActiveTrue(anyString()))
                .thenReturn(Optional.of(form));

        // Case 1: handicapped is false, disability_percentage is missing -> should pass
        Map<String, Object> request1 = new HashMap<>();
        request1.put("handicapped", false);
        assertDoesNotThrow(() -> validationService.validate("test-form", request1));

        // Case 2: handicapped is true, disability_percentage is missing -> should fail since it's mandatory when condition met
        Map<String, Object> request2 = new HashMap<>();
        request2.put("handicapped", true);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> validationService.validate("test-form", request2));
        assertTrue(ex.getErrors().containsKey("disability_percentage"));

        // Case 3: handicapped is true, disability_percentage is provided but not a number -> should fail type check
        Map<String, Object> request3 = new HashMap<>();
        request3.put("handicapped", true);
        request3.put("disability_percentage", "not-a-number");
        ValidationException ex2 = assertThrows(ValidationException.class,
                () -> validationService.validate("test-form", request3));
        assertEquals("Disability Percentage must be a valid number", ex2.getErrors().get("disability_percentage"));

        // Case 4: handicapped is true, disability_percentage is valid number -> should pass
        Map<String, Object> request4 = new HashMap<>();
        request4.put("handicapped", true);
        request4.put("disability_percentage", 45);
        assertDoesNotThrow(() -> validationService.validate("test-form", request4));
    }
}
