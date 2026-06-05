package com.crpf.forms.service;

import com.crpf.forms.dto.FormFieldMetadataResponse;
import com.crpf.forms.dto.FormMetadataResponse;
import com.crpf.forms.entity.FormMetadata;
import com.crpf.forms.exception.ResourceNotFoundException;
import com.crpf.forms.mapper.MetadataMapper;
import com.crpf.forms.repository.FormMetadataRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MetadataService {

    private final FormMetadataRepository formMetadataRepository;
    private final MetadataMapper metadataMapper;

    public MetadataService(FormMetadataRepository formMetadataRepository, MetadataMapper metadataMapper) {
        this.formMetadataRepository = formMetadataRepository;
        this.metadataMapper = metadataMapper;
    }

    public List<FormMetadataResponse> getAllForms() {
        return formMetadataRepository.findAllByActiveTrue().stream()
                .map(metadataMapper::toFormResponse)
                .collect(Collectors.toList());
    }

    public FormMetadataResponse getFormByName(String formName) {
        FormMetadata form = formMetadataRepository.findByFormNameIgnoreCaseAndActiveTrue(formName)
                .orElseThrow(() -> new ResourceNotFoundException("Form configuration not found for: " + formName));
        return metadataMapper.toFormResponse(form);
    }

    public List<FormFieldMetadataResponse> getFormFields(String formName) {
        FormMetadata form = formMetadataRepository.findByFormNameIgnoreCaseAndActiveTrue(formName)
                .orElseThrow(() -> new ResourceNotFoundException("Form configuration not found for: " + formName));
        return metadataMapper.toFieldResponseList(form.getFields());
    }
}
