package com.crpf.forms.service;

import com.crpf.forms.dto.FamilyMemberRequest;
import com.crpf.forms.dto.FamilyMemberResponse;
import com.crpf.forms.entity.FamilyMember;
import com.crpf.forms.exception.DuplicateRecordException;
import com.crpf.forms.exception.ValidationException;
import com.crpf.forms.mapper.FamilyMapper;
import com.crpf.forms.repository.FamilyMemberRepository;
import com.crpf.forms.validation.DynamicValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FamilyServiceTest {

    @Mock
    private FamilyMemberRepository familyMemberRepository;

    private final FamilyMapper familyMapper = new com.crpf.forms.mapper.FamilyMapperImpl();

    @Mock
    private DynamicValidationService dynamicValidationService;

    private FamilyService familyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        familyService = new FamilyService(familyMemberRepository, familyMapper, dynamicValidationService);
    }

    @Test
    void testCreateFamilyMemberSuccessAndCalculatesAge() {
        LocalDate dob = LocalDate.now().minusYears(25); // 25 years old
        FamilyMemberRequest request = FamilyMemberRequest.builder()
                .forceNo("F123")
                .memberName("Priya")
                .relationship("SPOUSE")
                .dob(dob)
                .aadhaarNumber("123456789012")
                .panNumber("ABCDE1234F")
                .mobileNumber("9876543210")
                .handicapped(true)
                .disabilityPercentage(new BigDecimal("40.0"))
                .build();

        when(familyMemberRepository.existsByForceNoAndMemberNameAndRelationshipAndActiveTrue(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(familyMemberRepository.save(any(FamilyMember.class))).thenAnswer(invocation -> {
            FamilyMember entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        FamilyMemberResponse result = familyService.createFamilyMember(request);

        assertNotNull(result);
        assertEquals(25, result.getAge());
        verify(dynamicValidationService, times(1)).validate("family", request);
        verify(familyMemberRepository, times(1)).save(any(FamilyMember.class));
    }

    @Test
    void testCreateFamilyMemberFutureDobThrowsException() {
        FamilyMemberRequest request = FamilyMemberRequest.builder()
                .forceNo("F123")
                .memberName("Priya")
                .relationship("SPOUSE")
                .dob(LocalDate.now().plusDays(1)) // Future dob
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> familyService.createFamilyMember(request));

        assertTrue(exception.getErrors().containsKey("dob"));
        assertEquals("Date of birth cannot be in the future", exception.getErrors().get("dob"));
    }

    @Test
    void testCreateFamilyMemberInvalidAadhaarThrowsException() {
        FamilyMemberRequest request = FamilyMemberRequest.builder()
                .forceNo("F123")
                .memberName("Priya")
                .relationship("SPOUSE")
                .dob(LocalDate.now().minusYears(5))
                .aadhaarNumber("12345") // Only 5 digits instead of 12
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> familyService.createFamilyMember(request));

        assertTrue(exception.getErrors().containsKey("aadhaarNumber"));
    }

    @Test
    void testCreateFamilyMemberHandicappedButNoDisabilityPercentageThrowsException() {
        FamilyMemberRequest request = FamilyMemberRequest.builder()
                .forceNo("F123")
                .memberName("Priya")
                .relationship("SPOUSE")
                .dob(LocalDate.now().minusYears(5))
                .handicapped(true)
                .disabilityPercentage(null) // Mandatory when handicapped is true
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> familyService.createFamilyMember(request));

        assertTrue(exception.getErrors().containsKey("disabilityPercentage"));
        assertEquals("Disability percentage is mandatory when handicapped", exception.getErrors().get("disabilityPercentage"));
    }

    @Test
    void testCreateFamilyMemberDuplicateThrowsException() {
        FamilyMemberRequest request = FamilyMemberRequest.builder()
                .forceNo("F123")
                .memberName("Priya")
                .relationship("SPOUSE")
                .dob(LocalDate.now().minusYears(5))
                .build();

        when(familyMemberRepository.existsByForceNoAndMemberNameAndRelationshipAndActiveTrue(anyString(), anyString(), anyString()))
                .thenReturn(true); // Duplicate exists

        assertThrows(DuplicateRecordException.class,
                () -> familyService.createFamilyMember(request));
    }
}
