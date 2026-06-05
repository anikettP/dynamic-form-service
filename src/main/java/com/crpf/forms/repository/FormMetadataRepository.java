package com.crpf.forms.repository;

import com.crpf.forms.entity.FormMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormMetadataRepository extends JpaRepository<FormMetadata, Long> {
    Optional<FormMetadata> findByFormNameIgnoreCaseAndActiveTrue(String formName);
    List<FormMetadata> findAllByActiveTrue();
}
