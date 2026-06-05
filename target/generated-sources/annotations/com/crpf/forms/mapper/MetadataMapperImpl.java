package com.crpf.forms.mapper;

import com.crpf.forms.dto.FormFieldMetadataResponse;
import com.crpf.forms.dto.FormMetadataResponse;
import com.crpf.forms.entity.FormFieldMetadata;
import com.crpf.forms.entity.FormMetadata;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-06T01:39:51+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class MetadataMapperImpl implements MetadataMapper {

    @Override
    public FormMetadataResponse toFormResponse(FormMetadata formMetadata) {
        if ( formMetadata == null ) {
            return null;
        }

        FormMetadataResponse.FormMetadataResponseBuilder formMetadataResponse = FormMetadataResponse.builder();

        formMetadataResponse.description( formMetadata.getDescription() );
        formMetadataResponse.fields( toFieldResponseList( formMetadata.getFields() ) );
        formMetadataResponse.formName( formMetadata.getFormName() );

        return formMetadataResponse.build();
    }

    @Override
    public FormFieldMetadataResponse toFieldResponse(FormFieldMetadata fieldMetadata) {
        if ( fieldMetadata == null ) {
            return null;
        }

        FormFieldMetadataResponse.FormFieldMetadataResponseBuilder formFieldMetadataResponse = FormFieldMetadataResponse.builder();

        formFieldMetadataResponse.dependsOnField( fieldMetadata.getDependsOnField() );
        formFieldMetadataResponse.dependsOnValue( fieldMetadata.getDependsOnValue() );
        formFieldMetadataResponse.editable( fieldMetadata.isEditable() );
        formFieldMetadataResponse.fieldName( fieldMetadata.getFieldName() );
        formFieldMetadataResponse.fieldType( fieldMetadata.getFieldType() );
        formFieldMetadataResponse.label( fieldMetadata.getLabel() );
        formFieldMetadataResponse.maxLength( fieldMetadata.getMaxLength() );
        formFieldMetadataResponse.minLength( fieldMetadata.getMinLength() );
        formFieldMetadataResponse.regexPattern( fieldMetadata.getRegexPattern() );
        formFieldMetadataResponse.required( fieldMetadata.isRequired() );
        formFieldMetadataResponse.visible( fieldMetadata.isVisible() );

        return formFieldMetadataResponse.build();
    }

    @Override
    public List<FormFieldMetadataResponse> toFieldResponseList(List<FormFieldMetadata> fieldMetadataList) {
        if ( fieldMetadataList == null ) {
            return null;
        }

        List<FormFieldMetadataResponse> list = new ArrayList<FormFieldMetadataResponse>( fieldMetadataList.size() );
        for ( FormFieldMetadata formFieldMetadata : fieldMetadataList ) {
            list.add( toFieldResponse( formFieldMetadata ) );
        }

        return list;
    }
}
