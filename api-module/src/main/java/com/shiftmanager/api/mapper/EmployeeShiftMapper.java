package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.EmployeeShiftDTO;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.ShiftRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the EmployeeShift entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class EmployeeShiftMapper implements EntityMapper<EmployeeShiftDTO, EmployeeShift> {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ShiftRepository shiftRepository;
    
    @Override
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", expression = "java(shift.getEmployee() != null ? shift.getEmployee().getFirstName() + ' ' + shift.getEmployee().getLastName() : null)")
    @Mapping(target = "shiftId", source = "shift.id")
    public abstract EmployeeShiftDTO toDto(EmployeeShift shift);
    
    @Override
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "shift", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract EmployeeShift toEntity(EmployeeShiftDTO dto);
    
    @Override
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "shift", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract EmployeeShift updateEntityFromDto(EmployeeShiftDTO dto, @MappingTarget EmployeeShift shift);
    
    /**
     * Set the employee and shift relations
     * @param dto The DTO
     * @param employeeShift The entity
     */
    @AfterMapping
    protected void setRelations(EmployeeShiftDTO dto, @MappingTarget EmployeeShift employeeShift) {
        // Set employee
        if (dto.getEmployeeId() != null) {
            employeeRepository.findById(dto.getEmployeeId())
                    .ifPresent(employeeShift::setEmployee);
        }
        
        // Set shift
        if (dto.getShiftId() != null) {
            shiftRepository.findById(dto.getShiftId())
                    .ifPresent(employeeShift::setShift);
        }
    }
}