package com.crpf.forms.service;

import com.crpf.forms.dto.EducationRecordRequest;
import com.crpf.forms.dto.EducationRecordResponse;
import com.crpf.forms.entity.EducationRecord;
import com.crpf.forms.exception.DuplicateRecordException;
import com.crpf.forms.exception.ValidationException;
import com.crpf.forms.mapper.EducationMapper;
import com.crpf.forms.repository.EducationRecordRepository;
import com.crpf.forms.validation.DynamicValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EducationServiceTest {

    @Mock
    private EducationRecordRepository educationRecordRepository;

    private final EducationMapper educationMapper = new com.crpf.forms.mapper.EducationMapperImpl();

    @Mock
    private DynamicValidationService dynamicValidationService;

    private EducationService educationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        educationService = new EducationService(educationRecordRepository, educationMapper, dynamicValidationService);
    }

    @Test
    void testCreateEducationRecordSuccess() {
        EducationRecordRequest request = EducationRecordRequest.builder()
                .forceNo("F123")
                .name("Aniket")
                .educationType("GRADUATION")
                .examPassed("B.Tech")
                .passingYear(LocalDate.of(2022, 1, 1))
                .subject1("Maths")
                .subject2("Physics")
                .subject3("Chemistry")
                .startDate(LocalDate.of(2018, 7, 1))
                .endDate(LocalDate.of(2022, 6, 30))
                .build();

        when(educationRecordRepository.existsByForceNoAndEducationTypeAndExamPassedAndActiveTrue(anyString(), anyString(), anyString()))
                .thenReturn(false);
        when(educationRecordRepository.save(any(EducationRecord.class))).thenAnswer(invocation -> {
            EducationRecord entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        EducationRecordResponse result = educationService.createEducationRecord(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("F123", result.getForceNo());
        assertEquals("Aniket", result.getName());
        verify(dynamicValidationService, times(1)).validate("education", request);
        verify(educationRecordRepository, times(1)).save(any(EducationRecord.class));
    }

    @Test
    void testCreateEducationRecordFuturePassingYearThrowsException() {
        EducationRecordRequest request = EducationRecordRequest.builder()
                .forceNo("F123")
                .name("Aniket")
                .educationType("GRADUATION")
                .examPassed("B.Tech")
                .passingYear(LocalDate.now().plusYears(1)) // Future year
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> educationService.createEducationRecord(request));

        assertTrue(exception.getErrors().containsKey("passingYear"));
        assertEquals("Passing year cannot be a future year", exception.getErrors().get("passingYear"));
    }

    @Test
    void testCreateEducationRecordInvalidDatesThrowsException() {
        EducationRecordRequest request = EducationRecordRequest.builder()
                .forceNo("F123")
                .name("Aniket")
                .educationType("GRADUATION")
                .examPassed("B.Tech")
                .startDate(LocalDate.of(2022, 7, 1))
                .endDate(LocalDate.of(2022, 6, 30)) // End date is before start date
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> educationService.createEducationRecord(request));

        assertTrue(exception.getErrors().containsKey("startDate"));
        assertEquals("Start date must be before end date", exception.getErrors().get("startDate"));
    }

    @Test
    void testCreateEducationRecordSubjectEqualityThrowsException() {
        EducationRecordRequest request = EducationRecordRequest.builder()
                .forceNo("F123")
                .name("Aniket")
                .educationType("GRADUATION")
                .examPassed("B.Tech")
                .subject1("Maths")
                .subject2("Maths") // Subject 2 equals Subject 1
                .build();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> educationService.createEducationRecord(request));

        assertTrue(exception.getErrors().containsKey("subject2"));
        assertEquals("Subject 2 cannot equal Subject 1", exception.getErrors().get("subject2"));
    }

    @Test
    void testCreateEducationRecordDuplicateThrowsException() {
        EducationRecordRequest request = EducationRecordRequest.builder()
                .forceNo("F123")
                .name("Aniket")
                .educationType("GRADUATION")
                .examPassed("B.Tech")
                .build();

        when(educationRecordRepository.existsByForceNoAndEducationTypeAndExamPassedAndActiveTrue(anyString(), anyString(), anyString()))
                .thenReturn(true); // Duplicate exists

        assertThrows(DuplicateRecordException.class,
                () -> educationService.createEducationRecord(request));
    }
}
