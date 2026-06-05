package com.crpf.forms.mapper;

import com.crpf.forms.dto.FormFieldMetadataResponse;
import com.crpf.forms.dto.FormMetadataResponse;
import com.crpf.forms.entity.FormFieldMetadata;
import com.crpf.forms.entity.FormMetadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MetadataMapper {

    MetadataMapper INSTANCE = Mappers.getMapper(MetadataMapper.class);

    FormMetadataResponse toFormResponse(FormMetadata formMetadata);

    FormFieldMetadataResponse toFieldResponse(FormFieldMetadata fieldMetadata);

    List<FormFieldMetadataResponse> toFieldResponseList(List<FormFieldMetadata> fieldMetadataList);
}
