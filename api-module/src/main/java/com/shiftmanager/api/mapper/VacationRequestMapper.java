package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.VacationRequestDTO;
import com.shiftmanager.api.model.VacationRequest;
import com.shiftmanager.api.repository.EmployeeRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Period;

/**
 * Mapper for the VacationRequest entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class VacationRequestMapper implements EntityMapper<VacationRequestDTO, VacationRequest> {
    
    @Autowired
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
    public abstract VacationRequest toEntity(VacationRequestDTO dto);
    
    @Override
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "reviewer", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract VacationRequest updateEntityFromDto(VacationRequestDTO dto, @MappingTarget VacationRequest request);
    
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