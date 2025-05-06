package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.EmployeeShiftDTO;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.model.Shift;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-06T01:20:51+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class EmployeeShiftMapperImpl extends EmployeeShiftMapper {

    @Override
    public EmployeeShiftDTO toDto(EmployeeShift shift) {
        if ( shift == null ) {
            return null;
        }

        EmployeeShiftDTO.EmployeeShiftDTOBuilder employeeShiftDTO = EmployeeShiftDTO.builder();

        employeeShiftDTO.employeeId( shiftEmployeeId( shift ) );
        employeeShiftDTO.shiftId( shiftShiftId( shift ) );
        employeeShiftDTO.id( shift.getId() );
        employeeShiftDTO.status( shift.getStatus() );

        employeeShiftDTO.employeeName( shift.getEmployee() != null ? shift.getEmployee().getFirstName() + ' ' + shift.getEmployee().getLastName() : null );

        return employeeShiftDTO.build();
    }

    @Override
    public EmployeeShift toEntity(EmployeeShiftDTO dto) {
        if ( dto == null ) {
            return null;
        }

        EmployeeShift employeeShift = new EmployeeShift();

        employeeShift.setId( dto.getId() );
        employeeShift.setStatus( dto.getStatus() );

        setRelations( dto, employeeShift );

        return employeeShift;
    }

    @Override
    public EmployeeShift updateEntityFromDto(EmployeeShiftDTO dto, EmployeeShift shift) {
        if ( dto == null ) {
            return shift;
        }

        shift.setId( dto.getId() );
        shift.setStatus( dto.getStatus() );

        setRelations( dto, shift );

        return shift;
    }

    private Long shiftEmployeeId(EmployeeShift employeeShift) {
        if ( employeeShift == null ) {
            return null;
        }
        Employee employee = employeeShift.getEmployee();
        if ( employee == null ) {
            return null;
        }
        Long id = employee.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private Long shiftShiftId(EmployeeShift employeeShift) {
        if ( employeeShift == null ) {
            return null;
        }
        Shift shift = employeeShift.getShift();
        if ( shift == null ) {
            return null;
        }
        Long id = shift.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
