package com.crpf.forms.repository;

import com.crpf.forms.entity.EducationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EducationRecordRepository extends JpaRepository<EducationRecord, Long> {
    List<EducationRecord> findAllByActiveTrue();
    Optional<EducationRecord> findByIdAndActiveTrue(Long id);

    boolean existsByForceNoAndEducationTypeAndExamPassedAndActiveTrue(
            String forceNo, String educationType, String examPassed);

    boolean existsByForceNoAndEducationTypeAndExamPassedAndIdNotAndActiveTrue(
            String forceNo, String educationType, String examPassed, Long id);
}
