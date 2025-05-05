package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.EmployeeDTO;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Location;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T22:02:41+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (N/A)"
)
@Component
public class EmployeeMapperImpl extends EmployeeMapper {

    @Override
    public EmployeeDTO toDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDTO.EmployeeDTOBuilder<?, ?> employeeDTO = EmployeeDTO.builder();

        employeeDTO.locationId( employeeLocationId( employee ) );
        employeeDTO.locationName( employeeLocationName( employee ) );
        employeeDTO.managerId( employeeManagerId( employee ) );
        employeeDTO.id( employee.getId() );
        employeeDTO.firstName( employee.getFirstName() );
        employeeDTO.lastName( employee.getLastName() );
        employeeDTO.email( employee.getEmail() );
        employeeDTO.phoneNumber( employee.getPhoneNumber() );
        employeeDTO.fullName( employee.getFullName() );
        employeeDTO.employeeId( employee.getEmployeeId() );
        employeeDTO.hireDate( employee.getHireDate() );

        employeeDTO.isActive( employee.isActive() );

        return employeeDTO.build();
    }

    @Override
    public Employee toEntity(EmployeeDTO employeeDTO) {
        if ( employeeDTO == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setId( employeeDTO.getId() );
        employee.setFirstName( employeeDTO.getFirstName() );
        employee.setLastName( employeeDTO.getLastName() );
        employee.setEmail( employeeDTO.getEmail() );
        employee.setPhoneNumber( employeeDTO.getPhoneNumber() );
        employee.setEmployeeId( employeeDTO.getEmployeeId() );
        employee.setHireDate( employeeDTO.getHireDate() );

        setRelations( employeeDTO, employee );

        return employee;
    }

    @Override
    public Employee updateEntityFromDto(EmployeeDTO employeeDTO, Employee employee) {
        if ( employeeDTO == null ) {
            return employee;
        }

        employee.setId( employeeDTO.getId() );
        employee.setFirstName( employeeDTO.getFirstName() );
        employee.setLastName( employeeDTO.getLastName() );
        employee.setEmail( employeeDTO.getEmail() );
        employee.setPhoneNumber( employeeDTO.getPhoneNumber() );
        employee.setEmployeeId( employeeDTO.getEmployeeId() );
        employee.setHireDate( employeeDTO.getHireDate() );

        setRelations( employeeDTO, employee );

        return employee;
    }

    private Long employeeLocationId(Employee employee) {
        if ( employee == null ) {
            return null;
        }
        Location location = employee.getLocation();
        if ( location == null ) {
            return null;
        }
        Long id = location.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String employeeLocationName(Employee employee) {
        if ( employee == null ) {
            return null;
        }
        Location location = employee.getLocation();
        if ( location == null ) {
            return null;
        }
        String name = location.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private Long employeeManagerId(Employee employee) {
        if ( employee == null ) {
            return null;
        }
        Employee manager = employee.getManager();
        if ( manager == null ) {
            return null;
        }
        Long id = manager.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
