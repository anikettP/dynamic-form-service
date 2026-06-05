package com.crpf.forms.mapper;

import com.crpf.forms.dto.FamilyMemberRequest;
import com.crpf.forms.dto.FamilyMemberResponse;
import com.crpf.forms.entity.FamilyMember;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-06T01:39:39+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class FamilyMapperImpl implements FamilyMapper {

    @Override
    public FamilyMember toEntity(FamilyMemberRequest request) {
        if ( request == null ) {
            return null;
        }

        FamilyMember.FamilyMemberBuilder familyMember = FamilyMember.builder();

        familyMember.aadhaarNumber( request.getAadhaarNumber() );
        familyMember.autisticMember( request.isAutisticMember() );
        familyMember.disabilityPercentage( request.getDisabilityPercentage() );
        familyMember.dob( request.getDob() );
        familyMember.employed( request.isEmployed() );
        familyMember.forceNo( request.getForceNo() );
        familyMember.handicapped( request.isHandicapped() );
        familyMember.memberName( request.getMemberName() );
        familyMember.memberType( request.getMemberType() );
        familyMember.mobileNumber( request.getMobileNumber() );
        familyMember.panNumber( request.getPanNumber() );
        familyMember.relationship( request.getRelationship() );
        familyMember.remarks( request.getRemarks() );

        return familyMember.build();
    }

    @Override
    public FamilyMemberResponse toResponse(FamilyMember entity) {
        if ( entity == null ) {
            return null;
        }

        FamilyMemberResponse.FamilyMemberResponseBuilder familyMemberResponse = FamilyMemberResponse.builder();

        familyMemberResponse.aadhaarNumber( entity.getAadhaarNumber() );
        familyMemberResponse.active( entity.isActive() );
        familyMemberResponse.age( entity.getAge() );
        familyMemberResponse.autisticMember( entity.isAutisticMember() );
        familyMemberResponse.createdAt( entity.getCreatedAt() );
        familyMemberResponse.createdBy( entity.getCreatedBy() );
        familyMemberResponse.disabilityPercentage( entity.getDisabilityPercentage() );
        familyMemberResponse.dob( entity.getDob() );
        familyMemberResponse.employed( entity.isEmployed() );
        familyMemberResponse.forceNo( entity.getForceNo() );
        familyMemberResponse.handicapped( entity.isHandicapped() );
        familyMemberResponse.id( entity.getId() );
        familyMemberResponse.memberName( entity.getMemberName() );
        familyMemberResponse.memberType( entity.getMemberType() );
        familyMemberResponse.mobileNumber( entity.getMobileNumber() );
        familyMemberResponse.panNumber( entity.getPanNumber() );
        familyMemberResponse.relationship( entity.getRelationship() );
        familyMemberResponse.remarks( entity.getRemarks() );
        familyMemberResponse.updatedAt( entity.getUpdatedAt() );
        familyMemberResponse.updatedBy( entity.getUpdatedBy() );

        return familyMemberResponse.build();
    }

    @Override
    public List<FamilyMemberResponse> toResponseList(List<FamilyMember> entities) {
        if ( entities == null ) {
            return null;
        }

        List<FamilyMemberResponse> list = new ArrayList<FamilyMemberResponse>( entities.size() );
        for ( FamilyMember familyMember : entities ) {
            list.add( toResponse( familyMember ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(FamilyMemberRequest request, FamilyMember entity) {
        if ( request == null ) {
            return;
        }

        entity.setAadhaarNumber( request.getAadhaarNumber() );
        entity.setAutisticMember( request.isAutisticMember() );
        entity.setDisabilityPercentage( request.getDisabilityPercentage() );
        entity.setDob( request.getDob() );
        entity.setEmployed( request.isEmployed() );
        entity.setForceNo( request.getForceNo() );
        entity.setHandicapped( request.isHandicapped() );
        entity.setMemberName( request.getMemberName() );
        entity.setMemberType( request.getMemberType() );
        entity.setMobileNumber( request.getMobileNumber() );
        entity.setPanNumber( request.getPanNumber() );
        entity.setRelationship( request.getRelationship() );
        entity.setRemarks( request.getRemarks() );
    }
}
