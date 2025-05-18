package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.EmployeeShiftDTO;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.ShiftRepository;
import lombok.AllArgsConstructor;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the EmployeeShift entity and its DTO
 */
/**
 * Mapper for the EmployeeShift entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {})
@AllArgsConstructor
public abstract class EmployeeShiftMapper implements EntityMapper<EmployeeShiftDTO, EmployeeShift> {

    private EmployeeRepository employeeRepository;
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
    @Mapping(target = "assignedBy", ignore = true)
    @Mapping(target = "clockInTime", expression = "java(null)")
    @Mapping(target = "clockOutTime", expression = "java(null)")
    @Mapping(target = "note", expression = "java(\"\")")
    @Mapping(target = "assignedById", expression = "java(null)")
    @Mapping(target = "assignedAt", expression = "java(java.time.LocalDateTime.now())")
    public abstract EmployeeShift toEntity(EmployeeShiftDTO dto);

    @Override
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "shift", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "assignedBy", ignore = true)
    @Mapping(target = "clockInTime", expression = "java(shift.getClockInTime())")
    @Mapping(target = "clockOutTime", expression = "java(shift.getClockOutTime())")
    @Mapping(target = "note", expression = "java(shift.getNote() != null ? shift.getNote() : \"\")")
    @Mapping(target = "assignedById", expression = "java(shift.getAssignedById())")
    @Mapping(target = "assignedAt", expression = "java(shift.getAssignedAt() != null ? shift.getAssignedAt() : java.time.LocalDateTime.now())")
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