package com.crpf.forms.mapper;

import com.crpf.forms.dto.EducationRecordRequest;
import com.crpf.forms.dto.EducationRecordResponse;
import com.crpf.forms.entity.EducationRecord;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-06-06T01:44:00+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class EducationMapperImpl implements EducationMapper {

    @Override
    public EducationRecord toEntity(EducationRecordRequest request) {
        if ( request == null ) {
            return null;
        }

        EducationRecord.EducationRecordBuilder educationRecord = EducationRecord.builder();

        educationRecord.educationType( request.getEducationType() );
        educationRecord.endDate( request.getEndDate() );
        educationRecord.examPassed( request.getExamPassed() );
        educationRecord.forceNo( request.getForceNo() );
        educationRecord.name( request.getName() );
        educationRecord.passingYear( request.getPassingYear() );
        educationRecord.rank( request.getRank() );
        educationRecord.remarks( request.getRemarks() );
        educationRecord.schoolName( request.getSchoolName() );
        educationRecord.startDate( request.getStartDate() );
        educationRecord.subject1( request.getSubject1() );
        educationRecord.subject2( request.getSubject2() );
        educationRecord.subject3( request.getSubject3() );
        educationRecord.unit( request.getUnit() );

        return educationRecord.build();
    }

    @Override
    public EducationRecordResponse toResponse(EducationRecord entity) {
        if ( entity == null ) {
            return null;
        }

        EducationRecordResponse.EducationRecordResponseBuilder educationRecordResponse = EducationRecordResponse.builder();

        educationRecordResponse.active( entity.isActive() );
        educationRecordResponse.createdAt( entity.getCreatedAt() );
        educationRecordResponse.createdBy( entity.getCreatedBy() );
        educationRecordResponse.educationType( entity.getEducationType() );
        educationRecordResponse.endDate( entity.getEndDate() );
        educationRecordResponse.examPassed( entity.getExamPassed() );
        educationRecordResponse.forceNo( entity.getForceNo() );
        educationRecordResponse.id( entity.getId() );
        educationRecordResponse.name( entity.getName() );
        educationRecordResponse.passingYear( entity.getPassingYear() );
        educationRecordResponse.rank( entity.getRank() );
        educationRecordResponse.remarks( entity.getRemarks() );
        educationRecordResponse.schoolName( entity.getSchoolName() );
        educationRecordResponse.startDate( entity.getStartDate() );
        educationRecordResponse.subject1( entity.getSubject1() );
        educationRecordResponse.subject2( entity.getSubject2() );
        educationRecordResponse.subject3( entity.getSubject3() );
        educationRecordResponse.unit( entity.getUnit() );
        educationRecordResponse.updatedAt( entity.getUpdatedAt() );
        educationRecordResponse.updatedBy( entity.getUpdatedBy() );

        return educationRecordResponse.build();
    }

    @Override
    public List<EducationRecordResponse> toResponseList(List<EducationRecord> entities) {
        if ( entities == null ) {
            return null;
        }

        List<EducationRecordResponse> list = new ArrayList<EducationRecordResponse>( entities.size() );
        for ( EducationRecord educationRecord : entities ) {
            list.add( toResponse( educationRecord ) );
        }

        return list;
    }

    @Override
    public void updateEntityFromRequest(EducationRecordRequest request, EducationRecord entity) {
        if ( request == null ) {
            return;
        }

        entity.setEducationType( request.getEducationType() );
        entity.setEndDate( request.getEndDate() );
        entity.setExamPassed( request.getExamPassed() );
        entity.setForceNo( request.getForceNo() );
        entity.setName( request.getName() );
        entity.setPassingYear( request.getPassingYear() );
        entity.setRank( request.getRank() );
        entity.setRemarks( request.getRemarks() );
        entity.setSchoolName( request.getSchoolName() );
        entity.setStartDate( request.getStartDate() );
        entity.setSubject1( request.getSubject1() );
        entity.setSubject2( request.getSubject2() );
        entity.setSubject3( request.getSubject3() );
        entity.setUnit( request.getUnit() );
    }
}
