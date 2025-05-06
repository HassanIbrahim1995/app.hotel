package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.VacationRequestDTO;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.VacationRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-06T01:20:51+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class VacationRequestMapperImpl extends VacationRequestMapper {

    @Override
    public VacationRequestDTO toDto(VacationRequest request) {
        if ( request == null ) {
            return null;
        }

        VacationRequestDTO.VacationRequestDTOBuilder vacationRequestDTO = VacationRequestDTO.builder();

        vacationRequestDTO.employeeId( requestEmployeeId( request ) );
        vacationRequestDTO.reviewerId( requestReviewerId( request ) );
        vacationRequestDTO.id( request.getId() );
        vacationRequestDTO.startDate( request.getStartDate() );
        vacationRequestDTO.endDate( request.getEndDate() );
        vacationRequestDTO.requestNotes( request.getRequestNotes() );
        vacationRequestDTO.status( request.getStatus() );
        vacationRequestDTO.reviewedAt( request.getReviewedAt() );
        vacationRequestDTO.reviewNotes( request.getReviewNotes() );

        vacationRequestDTO.employeeName( request.getEmployee() != null ? request.getEmployee().getFirstName() + ' ' + request.getEmployee().getLastName() : null );
        vacationRequestDTO.reviewerName( request.getReviewer() != null ? request.getReviewer().getFirstName() + ' ' + request.getReviewer().getLastName() : null );
        vacationRequestDTO.durationDays( Long.valueOf(request.getDays()).intValue() );

        return vacationRequestDTO.build();
    }

    @Override
    public VacationRequest toEntity(VacationRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        VacationRequest vacationRequest = new VacationRequest();

        vacationRequest.setId( dto.getId() );
        vacationRequest.setStartDate( dto.getStartDate() );
        vacationRequest.setEndDate( dto.getEndDate() );
        vacationRequest.setStatus( dto.getStatus() );
        vacationRequest.setRequestNotes( dto.getRequestNotes() );
        vacationRequest.setReviewNotes( dto.getReviewNotes() );
        vacationRequest.setReviewedAt( dto.getReviewedAt() );

        setRelations( dto, vacationRequest );

        return vacationRequest;
    }

    @Override
    public VacationRequest updateEntityFromDto(VacationRequestDTO dto, VacationRequest request) {
        if ( dto == null ) {
            return request;
        }

        request.setId( dto.getId() );
        request.setStartDate( dto.getStartDate() );
        request.setEndDate( dto.getEndDate() );
        request.setStatus( dto.getStatus() );
        request.setRequestNotes( dto.getRequestNotes() );
        request.setReviewNotes( dto.getReviewNotes() );
        request.setReviewedAt( dto.getReviewedAt() );

        setRelations( dto, request );

        return request;
    }

    private Long requestEmployeeId(VacationRequest vacationRequest) {
        if ( vacationRequest == null ) {
            return null;
        }
        Employee employee = vacationRequest.getEmployee();
        if ( employee == null ) {
            return null;
        }
        Long id = employee.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long requestReviewerId(VacationRequest vacationRequest) {
        if ( vacationRequest == null ) {
            return null;
        }
        Employee reviewer = vacationRequest.getReviewer();
        if ( reviewer == null ) {
            return null;
        }
        Long id = reviewer.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
