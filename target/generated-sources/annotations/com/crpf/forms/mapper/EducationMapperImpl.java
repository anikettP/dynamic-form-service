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
    date = "2026-06-08T19:30:12+0530",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class EducationMapperImpl implements EducationMapper {

    @Override
    public EducationRecord toEntity(EducationRecordRequest request) {
        if ( request == null ) {
            return null;
        }

        EducationRecord.EducationRecordBuilder educationRecord = EducationRecord.builder();

        educationRecord.forceNo( request.getForceNo() );
        educationRecord.educationType( request.getEducationType() );
        educationRecord.schoolName( request.getSchoolName() );
        educationRecord.passingYear( request.getPassingYear() );
        educationRecord.examPassed( request.getExamPassed() );
        educationRecord.subject1( request.getSubject1() );
        educationRecord.subject2( request.getSubject2() );
        educationRecord.subject3( request.getSubject3() );
        educationRecord.startDate( request.getStartDate() );
        educationRecord.endDate( request.getEndDate() );
        educationRecord.remarks( request.getRemarks() );

        return educationRecord.build();
    }

    @Override
    public EducationRecordResponse toResponse(EducationRecord entity) {
        if ( entity == null ) {
            return null;
        }

        EducationRecordResponse.EducationRecordResponseBuilder educationRecordResponse = EducationRecordResponse.builder();

        educationRecordResponse.id( entity.getId() );
        educationRecordResponse.forceNo( entity.getForceNo() );
        educationRecordResponse.educationType( entity.getEducationType() );
        educationRecordResponse.schoolName( entity.getSchoolName() );
        educationRecordResponse.passingYear( entity.getPassingYear() );
        educationRecordResponse.examPassed( entity.getExamPassed() );
        educationRecordResponse.subject1( entity.getSubject1() );
        educationRecordResponse.subject2( entity.getSubject2() );
        educationRecordResponse.subject3( entity.getSubject3() );
        educationRecordResponse.startDate( entity.getStartDate() );
        educationRecordResponse.endDate( entity.getEndDate() );
        educationRecordResponse.remarks( entity.getRemarks() );
        educationRecordResponse.active( entity.isActive() );
        educationRecordResponse.createdBy( entity.getCreatedBy() );
        educationRecordResponse.createdAt( entity.getCreatedAt() );
        educationRecordResponse.updatedBy( entity.getUpdatedBy() );
        educationRecordResponse.updatedAt( entity.getUpdatedAt() );

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

        entity.setForceNo( request.getForceNo() );
        entity.setEducationType( request.getEducationType() );
        entity.setSchoolName( request.getSchoolName() );
        entity.setPassingYear( request.getPassingYear() );
        entity.setExamPassed( request.getExamPassed() );
        entity.setSubject1( request.getSubject1() );
        entity.setSubject2( request.getSubject2() );
        entity.setSubject3( request.getSubject3() );
        entity.setStartDate( request.getStartDate() );
        entity.setEndDate( request.getEndDate() );
        entity.setRemarks( request.getRemarks() );
    }
}
