package com.crpf.forms.service;

import com.crpf.forms.dto.FamilyMemberRequest;
import com.crpf.forms.dto.FamilyMemberResponse;
import com.crpf.forms.entity.FamilyMember;
import com.crpf.forms.exception.DuplicateRecordException;
import com.crpf.forms.exception.ResourceNotFoundException;
import com.crpf.forms.exception.ValidationException;
import com.crpf.forms.mapper.FamilyMapper;
import com.crpf.forms.repository.FamilyMemberRepository;
import com.crpf.forms.validation.DynamicValidationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FamilyService {

    private final FamilyMemberRepository familyMemberRepository;
    private final FamilyMapper familyMapper;
    private final DynamicValidationService dynamicValidationService;

    public FamilyService(FamilyMemberRepository familyMemberRepository,
                         FamilyMapper familyMapper,
                         DynamicValidationService dynamicValidationService) {
        this.familyMemberRepository = familyMemberRepository;
        this.familyMapper = familyMapper;
        this.dynamicValidationService = dynamicValidationService;
    }

    @Transactional(readOnly = true)
    public List<FamilyMemberResponse> getAllFamilyMembers() {
        List<FamilyMember> members = familyMemberRepository.findAllByActiveTrue();
        return familyMapper.toResponseList(members);
    }

    @Transactional(readOnly = true)
    public FamilyMemberResponse getFamilyMemberById(Long id) {
        FamilyMember member = familyMemberRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Family member not found with id: " + id));
        return familyMapper.toResponse(member);
    }

    public FamilyMemberResponse createFamilyMember(FamilyMemberRequest request) {
        // 1. Dynamic metadata validation
        dynamicValidationService.validate("family", request);

        // 2. Custom Business rules
        validateBusinessRules(request);

        // 3. Duplicate check
        if (familyMemberRepository.existsByForceNoAndMemberNameAndRelationshipAndActiveTrue(
                request.getForceNo(), request.getMemberName(), request.getRelationship())) {
            throw new DuplicateRecordException("Duplicate family member already exists for Force No: " +
                    request.getForceNo() + ", Member Name: " + request.getMemberName() +
                    ", Relationship: " + request.getRelationship());
        }

        FamilyMember entity = familyMapper.toEntity(request);
        
        // Auto-calculate age
        calculateAge(entity, request.getDob());

        FamilyMember saved = familyMemberRepository.save(entity);
        return familyMapper.toResponse(saved);
    }

    public FamilyMemberResponse updateFamilyMember(Long id, FamilyMemberRequest request) {
        FamilyMember existing = familyMemberRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Family member not found with id: " + id));

        // 1. Dynamic metadata validation
        dynamicValidationService.validate("family", request);

        // 2. Custom Business rules
        validateBusinessRules(request);

        // 3. Duplicate check excluding this record
        if (familyMemberRepository.existsByForceNoAndMemberNameAndRelationshipAndIdNotAndActiveTrue(
                request.getForceNo(), request.getMemberName(), request.getRelationship(), id)) {
            throw new DuplicateRecordException("Another family member already exists for Force No: " +
                    request.getForceNo() + ", Member Name: " + request.getMemberName() +
                    ", Relationship: " + request.getRelationship());
        }

        familyMapper.updateEntityFromRequest(request, existing);
        
        // Auto-calculate age
        calculateAge(existing, request.getDob());

        FamilyMember saved = familyMemberRepository.save(existing);
        return familyMapper.toResponse(saved);
    }

    public void deleteFamilyMember(Long id) {
        FamilyMember existing = familyMemberRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Family member not found with id: " + id));

        // Perform Soft Delete
        existing.setActive(false);
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        existing.setDeletedBy(currentUser);
        existing.setDeletedAt(OffsetDateTime.now());

        familyMemberRepository.save(existing);
    }

    private void calculateAge(FamilyMember entity, LocalDate dob) {
        if (dob != null) {
            entity.setAge(Period.between(dob, LocalDate.now()).getYears());
        } else {
            entity.setAge(null);
        }
    }

    private void validateBusinessRules(FamilyMemberRequest request) {
        Map<String, String> errors = new HashMap<>();

        // Rule 1: DOB cannot be future
        if (request.getDob() != null) {
            if (request.getDob().isAfter(LocalDate.now())) {
                errors.put("dob", "Date of birth cannot be in the future");
            }
        }

        // Rule 2: Aadhaar validation
        if (StringUtils.hasText(request.getAadhaarNumber())) {
            if (!request.getAadhaarNumber().matches("^\\d{12}$")) {
                errors.put("aadhaarNumber", "Aadhaar number must be exactly 12 digits");
            }
        }

        // Rule 3: PAN validation
        if (StringUtils.hasText(request.getPanNumber())) {
            if (!request.getPanNumber().matches("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")) {
                errors.put("panNumber", "PAN number must match standard format (e.g. ABCDE1234F)");
            }
        }

        // Rule 4: Mobile validation
        if (StringUtils.hasText(request.getMobileNumber())) {
            if (!request.getMobileNumber().matches("^[6-9]\\d{9}$")) {
                errors.put("mobileNumber", "Mobile number must be a valid 10-digit number starting with 6-9");
            }
        }

        // Rule 5: Disability percentage mandatory when handicapped
        if (request.isHandicapped()) {
            if (request.getDisabilityPercentage() == null) {
                errors.put("disabilityPercentage", "Disability percentage is mandatory when handicapped");
            } else if (request.getDisabilityPercentage().compareTo(BigDecimal.ZERO) <= 0 ||
                    request.getDisabilityPercentage().compareTo(new BigDecimal("100")) > 0) {
                errors.put("disabilityPercentage", "Disability percentage must be between 1 and 100");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
