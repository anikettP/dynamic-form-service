package com.crpf.forms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "form_field_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormFieldMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    @JsonIgnore
    private FormMetadata form;

    @Column(name = "field_name", nullable = false, length = 100)
    private String fieldName;

    @Column(nullable = false, length = 100)
    private String label;

    @Column(name = "field_type", nullable = false, length = 50)
    private String fieldType; // TEXT, NUMBER, DATE, BOOLEAN

    @Column(nullable = false)
    @Builder.Default
    private boolean required = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean visible = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean editable = true;

    @Column(name = "min_length")
    private Integer minLength;

    @Column(name = "max_length")
    private Integer maxLength;

    @Column(name = "regex_pattern", length = 255)
    private String regexPattern;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "depends_on_field", length = 100)
    private String dependsOnField;

    @Column(name = "depends_on_value", length = 100)
    private String dependsOnValue;
}
