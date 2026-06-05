package com.crpf.forms.mapper;

import com.crpf.forms.dto.EducationRecordRequest;
import com.crpf.forms.dto.EducationRecordResponse;
import com.crpf.forms.entity.EducationRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EducationMapper {

    EducationMapper INSTANCE = Mappers.getMapper(EducationMapper.class);

    EducationRecord toEntity(EducationRecordRequest request);

    EducationRecordResponse toResponse(EducationRecord entity);

    List<EducationRecordResponse> toResponseList(List<EducationRecord> entities);

    void updateEntityFromRequest(EducationRecordRequest request, @MappingTarget EducationRecord entity);
}
