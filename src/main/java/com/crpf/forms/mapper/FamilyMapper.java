package com.crpf.forms.mapper;

import com.crpf.forms.dto.FamilyMemberRequest;
import com.crpf.forms.dto.FamilyMemberResponse;
import com.crpf.forms.entity.FamilyMember;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FamilyMapper {

    FamilyMapper INSTANCE = Mappers.getMapper(FamilyMapper.class);

    FamilyMember toEntity(FamilyMemberRequest request);

    FamilyMemberResponse toResponse(FamilyMember entity);

    List<FamilyMemberResponse> toResponseList(List<FamilyMember> entities);

    void updateEntityFromRequest(FamilyMemberRequest request, @MappingTarget FamilyMember entity);
}
