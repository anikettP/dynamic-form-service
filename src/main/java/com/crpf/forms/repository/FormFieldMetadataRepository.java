package com.crpf.forms.repository;

import com.crpf.forms.entity.FormFieldMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormFieldMetadataRepository extends JpaRepository<FormFieldMetadata, Long> {
    List<FormFieldMetadata> findByFormIdOrderByDisplayOrderAsc(Long formId);
}
