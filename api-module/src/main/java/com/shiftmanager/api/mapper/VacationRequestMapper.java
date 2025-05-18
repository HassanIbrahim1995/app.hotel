package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.VacationRequestDTO;
import com.shiftmanager.api.model.VacationRequest;
import com.shiftmanager.api.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.shiftmanager.api.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Mapper for the VacationRequest entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {})
@AllArgsConstructor
public abstract class VacationRequestMapper implements EntityMapper<VacationRequestDTO, VacationRequest> {

    private EmployeeRepository employeeRepository;

    @Override
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(request.getEmployee() != null ? request.getEmployee().getFirstName() + ' ' + request.getEmployee().getLastName() : null)")
    @Mapping(target = "reviewerId", source = "reviewer.id")
    @Mapping(target = "reviewerName", expression = "java(request.getReviewer() != null ? request.getReviewer().getFirstName() + ' ' + request.getReviewer().getLastName() : null)")
    @Mapping(target = "durationDays", expression = "java(Long.valueOf(request.getDays()).intValue())")
    public abstract VacationRequestDTO toDto(VacationRequest request);

    @Override
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "reviewedBy", expression = "java(mapReviewerIdToEmployee(dto.getReviewerId()))")
    @Mapping(target = "reviewDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "requestDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "reason", source = "status")
    public abstract VacationRequest toEntity(VacationRequestDTO dto);

    @Override
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "reviewedBy", expression = "java(mapReviewerIdToEmployee(dto.getReviewerId()))")
    @Mapping(target = "reviewDate", expression = "java(request.getReviewDate() != null ? request.getReviewDate() : java.time.LocalDate.now())")
    @Mapping(target = "requestDate", expression = "java(request.getRequestDate() != null ? request.getRequestDate() : java.time.LocalDate.now())")
    @Mapping(target = "reason", source = "status")
    public abstract VacationRequest updateEntityFromDto(VacationRequestDTO dto, @MappingTarget VacationRequest request);

    /**
     * Convert reviewerId to Employee
     */
    protected Employee mapReviewerIdToEmployee(Long reviewerId) {
        if (reviewerId == null) {
            return null;
        }
        return employeeRepository.findById(reviewerId).orElse(null);
    }

    /**
     * Set employee and reviewer relations
     * @param dto The DTO
     * @param request The entity
     */
    @AfterMapping
    protected void setRelations(VacationRequestDTO dto, @MappingTarget VacationRequest request) {
        // Set employee
        if (dto.getEmployeeId() != null) {
            employeeRepository.findById(dto.getEmployeeId())
                    .ifPresent(request::setEmployee);
        }

        // Set reviewer
        if (dto.getReviewerId() != null) {
            employeeRepository.findById(dto.getReviewerId())
                    .ifPresent(request::setReviewer);
        }
    }
}
