package com.crpf.forms.repository;

import com.crpf.forms.config.AuditConfig;
import com.crpf.forms.entity.EducationRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class EducationRecordRepositoryTest {

    @Autowired
    private EducationRecordRepository repository;

    @Test
    @WithMockUser(username = "test_auditor")
    void testSaveRecordAndAuditing() {
        EducationRecord record = EducationRecord.builder()
                .forceNo("F999")
                .educationType("MATRICULATION")
                .examPassed("10th Standard")
                .passingYear(LocalDate.of(2015, 1, 1))
                .active(true)
                .build();

        EducationRecord saved = repository.saveAndFlush(record);

        assertNotNull(saved.getId());
        assertEquals("test_auditor", saved.getCreatedBy());
        assertNotNull(saved.getCreatedAt());

        // Verify lookup by active status
        List<EducationRecord> activeRecords = repository.findAllByActiveTrue();
        assertFalse(activeRecords.isEmpty());
        assertTrue(activeRecords.stream().anyMatch(r -> r.getForceNo().equals("F999")));

        Optional<EducationRecord> lookup = repository.findByIdAndActiveTrue(saved.getId());
        assertTrue(lookup.isPresent());
    }

    @Test
    @WithMockUser(username = "test_auditor")
    void testDuplicateVerificationChecks() {
        EducationRecord record = EducationRecord.builder()
                .forceNo("F100")
                .educationType("DIPLOMA")
                .examPassed("Computer Science")
                .passingYear(LocalDate.of(2020, 1, 1))
                .active(true)
                .build();
        repository.saveAndFlush(record);

        boolean duplicateExists = repository.existsByForceNoAndEducationTypeAndExamPassedAndActiveTrue(
                "F100", "DIPLOMA", "Computer Science");
        assertTrue(duplicateExists);

        boolean nonDuplicate = repository.existsByForceNoAndEducationTypeAndExamPassedAndActiveTrue(
                "F100", "DIPLOMA", "Mechanical");
        assertFalse(nonDuplicate);
    }
}
