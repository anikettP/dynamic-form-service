package com.crpf.forms.validation;

import com.crpf.forms.entity.FormFieldMetadata;
import com.crpf.forms.entity.FormMetadata;
import com.crpf.forms.exception.ResourceNotFoundException;
import com.crpf.forms.exception.ValidationException;
import com.crpf.forms.repository.FormMetadataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Slf4j
public class DynamicValidationService {

    private final FormMetadataRepository formMetadataRepository;
    private final ObjectMapper objectMapper;

    public DynamicValidationService(FormMetadataRepository formMetadataRepository, ObjectMapper objectMapper) {
        this.formMetadataRepository = formMetadataRepository;
        this.objectMapper = objectMapper;
    }

    public void validate(String formName, Object requestDto) {
        FormMetadata form = formMetadataRepository.findByFormNameIgnoreCaseAndActiveTrue(formName)
                .orElseThrow(() -> new ResourceNotFoundException("Form configuration not found for: " + formName));

        // Convert DTO to Map for dynamic validation
        @SuppressWarnings("unchecked")
        Map<String, Object> data = objectMapper.convertValue(requestDto, Map.class);
        validateMap(form, data);
    }

    public void validateMap(FormMetadata form, Map<String, Object> data) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FormFieldMetadata field : form.getFields()) {
            String fieldName = field.getFieldName();
            Object rawValue = data.get(fieldName);

            // 1. Conditional dependency check
            boolean dependencyMet = true;
            if (StringUtils.hasText(field.getDependsOnField())) {
                Object depValue = data.get(field.getDependsOnField());
                String expectedVal = field.getDependsOnValue();
                
                String depValStr = depValue == null ? "" : depValue.toString();
                if (!depValStr.equalsIgnoreCase(expectedVal)) {
                    dependencyMet = false;
                }
            }

            // If dependency is active and it's disability_percentage when handicapped = true
            boolean isMandatory = field.isRequired();
            if (dependencyMet && "disability_percentage".equals(fieldName)) {
                isMandatory = true; // explicitly mandatory when handicap is true
            }

            // 2. Required validation
            boolean isMissing = isValueMissing(rawValue);
            if (isMandatory && (!dependencyMet || isMissing)) {
                // If it depends on a field and the dependency condition is met, it must be present
                if (dependencyMet && isMissing) {
                    errors.put(fieldName, field.getLabel() + " is required");
                    continue;
                }
            }

            // Skip remaining checks if the value is missing and not required
            if (isMissing) {
                continue;
            }

            // 3. Type Validation (Numeric & Date)
            String type = field.getFieldType();
            if ("NUMBER".equalsIgnoreCase(type)) {
                try {
                    new BigDecimal(rawValue.toString());
                } catch (NumberFormatException e) {
                    errors.put(fieldName, field.getLabel() + " must be a valid number");
                    continue;
                }
            } else if ("DATE".equalsIgnoreCase(type)) {
                try {
                    if (rawValue instanceof String) {
                        LocalDate.parse((String) rawValue);
                    } else if (rawValue instanceof List) {
                        // Jackson format array [yyyy, MM, dd]
                        @SuppressWarnings("unchecked")
                        List<Integer> list = (List<Integer>) rawValue;
                        if (list.size() >= 3) {
                            LocalDate.of(list.get(0), list.get(1), list.get(2));
                        } else {
                            throw new DateTimeParseException("Invalid array size", rawValue.toString(), 0);
                        }
                    }
                } catch (Exception e) {
                    errors.put(fieldName, field.getLabel() + " must be a valid date (YYYY-MM-DD)");
                    continue;
                }
            }

            // 4. Length Validation
            String valueStr = rawValue.toString();
            if (field.getMinLength() != null && valueStr.length() < field.getMinLength()) {
                errors.put(fieldName, field.getLabel() + " must be at least " + field.getMinLength() + " characters");
                continue;
            }
            if (field.getMaxLength() != null && valueStr.length() > field.getMaxLength()) {
                errors.put(fieldName, field.getLabel() + " must not exceed " + field.getMaxLength() + " characters");
                continue;
            }

            // 5. Regex Validation
            if (StringUtils.hasText(field.getRegexPattern())) {
                if (!valueStr.matches(field.getRegexPattern())) {
                    errors.put(fieldName, field.getLabel() + " format is invalid");
                    continue;
                }
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private boolean isValueMissing(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            return !StringUtils.hasText((String) value);
        }
        if (value instanceof Collection) {
            return ((Collection<?>) value).isEmpty();
        }
        return false;
    }
}
