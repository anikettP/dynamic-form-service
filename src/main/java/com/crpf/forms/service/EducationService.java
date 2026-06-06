package com.crpf.forms.service;

import com.crpf.forms.dto.EducationRecordRequest;
import com.crpf.forms.dto.EducationRecordResponse;
import com.crpf.forms.entity.EducationRecord;
import com.crpf.forms.exception.DuplicateRecordException;
import com.crpf.forms.exception.ResourceNotFoundException;
import com.crpf.forms.exception.ValidationException;
import com.crpf.forms.mapper.EducationMapper;
import com.crpf.forms.repository.EducationRecordRepository;
import com.crpf.forms.validation.DynamicValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EducationService {

    private final EducationRecordRepository educationRecordRepository;
    private final EducationMapper educationMapper;
    private final DynamicValidationService dynamicValidationService;

    public EducationService(EducationRecordRepository educationRecordRepository,
                            EducationMapper educationMapper,
                            DynamicValidationService dynamicValidationService) {
        this.educationRecordRepository = educationRecordRepository;
        this.educationMapper = educationMapper;
        this.dynamicValidationService = dynamicValidationService;
    }

    @Transactional(readOnly = true)
    public List<EducationRecordResponse> getAllEducationRecords() {
        List<EducationRecord> records = educationRecordRepository.findAllByActiveTrue();
        return educationMapper.toResponseList(records);
    }

    @Transactional(readOnly = true)
    public EducationRecordResponse getEducationRecordById(Long id) {
        EducationRecord record = educationRecordRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Education record not found with id: " + id));

        return educationMapper.toResponse(record);
    }

    public EducationRecordResponse createEducationRecord(
            EducationRecordRequest request) {

        dynamicValidationService.validate("education", request);

        validateBusinessRules(request);

        if (educationRecordRepository
                .existsByForceNoAndEducationTypeAndExamPassedAndActiveTrue(
                        request.getForceNo(),
                        request.getEducationType(),
                        request.getExamPassed())) {

            throw new DuplicateRecordException(
                    "Duplicate education record already exists");
        }

        EducationRecord entity = educationMapper.toEntity(request);

        EducationRecord saved =
                educationRecordRepository.save(entity);

        return educationMapper.toResponse(saved);
    }

    public EducationRecordResponse updateEducationRecord(
            Long id,
            EducationRecordRequest request) {

        EducationRecord existing =
                educationRecordRepository.findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Education record not found with id: " + id));

        dynamicValidationService.validate("education", request);

        validateBusinessRules(request);

        if (educationRecordRepository
                .existsByForceNoAndEducationTypeAndExamPassedAndIdNotAndActiveTrue(
                        request.getForceNo(),
                        request.getEducationType(),
                        request.getExamPassed(),
                        id)) {

            throw new DuplicateRecordException(
                    "Duplicate education record already exists");
        }

        educationMapper.updateEntityFromRequest(request, existing);

        EducationRecord updated =
                educationRecordRepository.save(existing);

        return educationMapper.toResponse(updated);
    }

    public void deleteEducationRecord(Long id) {

        EducationRecord existing =
                educationRecordRepository.findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Education record not found with id: " + id));

        existing.setActive(false);

        String currentUser = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        existing.setDeletedBy(currentUser);
        existing.setDeletedAt(OffsetDateTime.now());

        educationRecordRepository.save(existing);
    }

    private void validateBusinessRules(
            EducationRecordRequest request) {

        Map<String, String> errors = new HashMap<>();

        if (request.getPassingYear() != null) {

            int currentYear = LocalDate.now().getYear();

            if (request.getPassingYear().getYear() > currentYear) {
                errors.put(
                        "passingYear",
                        "Passing year cannot be a future year");
            }
        }

        if (request.getStartDate() != null &&
                request.getEndDate() != null &&
                !request.getStartDate().isBefore(request.getEndDate())) {

            errors.put(
                    "startDate",
                    "Start date must be before end date");
        }

        String subject1 = request.getSubject1();
        String subject2 = request.getSubject2();
        String subject3 = request.getSubject3();

        if (StringUtils.hasText(subject1)
                && StringUtils.hasText(subject2)
                && subject1.equalsIgnoreCase(subject2)) {

            errors.put(
                    "subject2",
                    "Subject 2 cannot equal Subject 1");
        }

        if (StringUtils.hasText(subject3)) {

            if (StringUtils.hasText(subject1)
                    && subject3.equalsIgnoreCase(subject1)) {

                errors.put(
                        "subject3",
                        "Subject 3 cannot be same as Subject 1");
            }

            if (StringUtils.hasText(subject2)
                    && subject3.equalsIgnoreCase(subject2)) {

                errors.put(
                        "subject3",
                        "Subject 3 cannot be same as Subject 2");
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public Boolean createEducationRecord(String forceNo, Map<String, Object> formData) {
        EducationRecord entity = new EducationRecord();
        entity.setForceNo(forceNo);
        entity.setFormData(formData);
        entity.setActive(true);
        EducationRecord saved = educationRecordRepository.save(entity);
        return Boolean.TRUE;
    }

    public Boolean updateEducationRecord(Long id, Map<String, Object> formData) {
        EducationRecord existing =
                educationRecordRepository.findByIdAndActiveTrue(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Education record not found with id: " + id));
        existing.setFormData(formData);
        return Boolean.TRUE;
    }
}