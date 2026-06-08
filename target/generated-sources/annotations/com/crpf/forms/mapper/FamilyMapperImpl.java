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
    date = "2026-06-08T19:30:12+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class FamilyMapperImpl implements FamilyMapper {

    @Override
    public FamilyMember toEntity(FamilyMemberRequest request) {
        if ( request == null ) {
            return null;
        }

        FamilyMember.FamilyMemberBuilder familyMember = FamilyMember.builder();

        familyMember.forceNo( request.getForceNo() );
        familyMember.memberName( request.getMemberName() );
        familyMember.dob( request.getDob() );
        familyMember.relationship( request.getRelationship() );
        familyMember.memberType( request.getMemberType() );
        familyMember.employed( request.isEmployed() );
        familyMember.handicapped( request.isHandicapped() );
        familyMember.disabilityPercentage( request.getDisabilityPercentage() );
        familyMember.autisticMember( request.isAutisticMember() );
        familyMember.aadhaarNumber( request.getAadhaarNumber() );
        familyMember.panNumber( request.getPanNumber() );
        familyMember.mobileNumber( request.getMobileNumber() );
        familyMember.remarks( request.getRemarks() );

        return familyMember.build();
    }

    @Override
    public FamilyMemberResponse toResponse(FamilyMember entity) {
        if ( entity == null ) {
            return null;
        }

        FamilyMemberResponse.FamilyMemberResponseBuilder familyMemberResponse = FamilyMemberResponse.builder();

        familyMemberResponse.id( entity.getId() );
        familyMemberResponse.forceNo( entity.getForceNo() );
        familyMemberResponse.memberName( entity.getMemberName() );
        familyMemberResponse.dob( entity.getDob() );
        familyMemberResponse.age( entity.getAge() );
        familyMemberResponse.relationship( entity.getRelationship() );
        familyMemberResponse.memberType( entity.getMemberType() );
        familyMemberResponse.employed( entity.isEmployed() );
        familyMemberResponse.handicapped( entity.isHandicapped() );
        familyMemberResponse.disabilityPercentage( entity.getDisabilityPercentage() );
        familyMemberResponse.autisticMember( entity.isAutisticMember() );
        familyMemberResponse.aadhaarNumber( entity.getAadhaarNumber() );
        familyMemberResponse.panNumber( entity.getPanNumber() );
        familyMemberResponse.mobileNumber( entity.getMobileNumber() );
        familyMemberResponse.remarks( entity.getRemarks() );
        familyMemberResponse.active( entity.isActive() );
        familyMemberResponse.createdBy( entity.getCreatedBy() );
        familyMemberResponse.createdAt( entity.getCreatedAt() );
        familyMemberResponse.updatedBy( entity.getUpdatedBy() );
        familyMemberResponse.updatedAt( entity.getUpdatedAt() );

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

        entity.setForceNo( request.getForceNo() );
        entity.setMemberName( request.getMemberName() );
        entity.setDob( request.getDob() );
        entity.setRelationship( request.getRelationship() );
        entity.setMemberType( request.getMemberType() );
        entity.setEmployed( request.isEmployed() );
        entity.setHandicapped( request.isHandicapped() );
        entity.setDisabilityPercentage( request.getDisabilityPercentage() );
        entity.setAutisticMember( request.isAutisticMember() );
        entity.setAadhaarNumber( request.getAadhaarNumber() );
        entity.setPanNumber( request.getPanNumber() );
        entity.setMobileNumber( request.getMobileNumber() );
        entity.setRemarks( request.getRemarks() );
    }
}
