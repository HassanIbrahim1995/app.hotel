package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.AddressDTO;
import com.shiftmanager.api.dto.EmployeeDTO;
import com.shiftmanager.api.model.Address;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Location;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T01:56:19+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public EmployeeDTO toDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setManagerId( employeeManagerId( employee ) );
        employeeDTO.setManagerName( employeeManagerFullName( employee ) );
        employeeDTO.setId( employee.getId() );
        employeeDTO.setFirstName( employee.getFirstName() );
        employeeDTO.setLastName( employee.getLastName() );
        employeeDTO.setEmail( employee.getEmail() );
        employeeDTO.setPhoneNumber( employee.getPhoneNumber() );
        employeeDTO.setAddress( addressMapper.toDTO( employee.getAddress() ) );
        employeeDTO.setEmployeeNumber( employee.getEmployeeNumber() );
        employeeDTO.setPosition( employee.getPosition() );
        employeeDTO.setDepartment( employee.getDepartment() );
        employeeDTO.setHireDate( employee.getHireDate() );
        employeeDTO.setHourlyRate( employee.getHourlyRate() );
        employeeDTO.setFullTime( employee.getFullTime() );
        employeeDTO.setMaxHoursPerWeek( employee.getMaxHoursPerWeek() );
        employeeDTO.setNote( employee.getNote() );

        return employeeDTO;
    }

    @Override
    public EmployeeDTO toDTO(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setManagerId( employeeManagerId( employee ) );
        employeeDTO.setManagerName( employeeManagerFullName( employee ) );
        employeeDTO.setId( employee.getId() );
        employeeDTO.setFirstName( employee.getFirstName() );
        employeeDTO.setLastName( employee.getLastName() );
        employeeDTO.setEmail( employee.getEmail() );
        employeeDTO.setPhoneNumber( employee.getPhoneNumber() );
        employeeDTO.setAddress( addressMapper.toDTO( employee.getAddress() ) );
        employeeDTO.setEmployeeNumber( employee.getEmployeeNumber() );
        employeeDTO.setPosition( employee.getPosition() );
        employeeDTO.setDepartment( employee.getDepartment() );
        employeeDTO.setHireDate( employee.getHireDate() );
        employeeDTO.setHourlyRate( employee.getHourlyRate() );
        employeeDTO.setFullTime( employee.getFullTime() );
        employeeDTO.setMaxHoursPerWeek( employee.getMaxHoursPerWeek() );
        employeeDTO.setNote( employee.getNote() );

        return employeeDTO;
    }

    @Override
    public Employee toEntity(EmployeeDTO employeeDTO) {
        if ( employeeDTO == null ) {
            return null;
        }

        Employee employee = new Employee();

        if ( employeeDTO.getId() != null ) {
            employee.setEmployeeId( String.valueOf( employeeDTO.getId() ) );
        }
        employee.setJobTitle( employeeDTO.getPosition() );
        employee.setLocation( addressDTOToLocation( employeeDTO.getAddress() ) );
        employee.setId( employeeDTO.getId() );
        employee.setFirstName( employeeDTO.getFirstName() );
        employee.setLastName( employeeDTO.getLastName() );
        employee.setEmail( employeeDTO.getEmail() );
        employee.setPhoneNumber( employeeDTO.getPhoneNumber() );
        employee.setAddress( addressMapper.toEntity( employeeDTO.getAddress() ) );
        employee.setEmployeeNumber( employeeDTO.getEmployeeNumber() );
        employee.setPosition( employeeDTO.getPosition() );
        employee.setDepartment( employeeDTO.getDepartment() );
        employee.setHireDate( employeeDTO.getHireDate() );
        employee.setHourlyRate( employeeDTO.getHourlyRate() );
        employee.setFullTime( employeeDTO.getFullTime() );
        employee.setMaxHoursPerWeek( employeeDTO.getMaxHoursPerWeek() );
        employee.setNote( employeeDTO.getNote() );

        employee.setStatus( "ACTIVE" );
        employee.setSubordinates( new java.util.ArrayList<>() );

        return employee;
    }

    @Override
    public void updateEntityFromDTO(EmployeeDTO employeeDTO, Employee employee) {
        if ( employeeDTO == null ) {
            return;
        }

        if ( employeeDTO.getId() != null ) {
            employee.setEmployeeId( String.valueOf( employeeDTO.getId() ) );
        }
        else {
            employee.setEmployeeId( null );
        }
        employee.setJobTitle( employeeDTO.getPosition() );
        if ( employeeDTO.getAddress() != null ) {
            if ( employee.getLocation() == null ) {
                employee.setLocation( new Location() );
            }
            addressDTOToLocation1( employeeDTO.getAddress(), employee.getLocation() );
        }
        else {
            employee.setLocation( null );
        }
        employee.setId( employeeDTO.getId() );
        employee.setFirstName( employeeDTO.getFirstName() );
        employee.setLastName( employeeDTO.getLastName() );
        employee.setEmail( employeeDTO.getEmail() );
        employee.setPhoneNumber( employeeDTO.getPhoneNumber() );
        if ( employeeDTO.getAddress() != null ) {
            if ( employee.getAddress() == null ) {
                employee.setAddress( new Address() );
            }
            addressMapper.updateEntityFromDTO( employeeDTO.getAddress(), employee.getAddress() );
        }
        else {
            employee.setAddress( null );
        }
        employee.setEmployeeNumber( employeeDTO.getEmployeeNumber() );
        employee.setPosition( employeeDTO.getPosition() );
        employee.setDepartment( employeeDTO.getDepartment() );
        employee.setHireDate( employeeDTO.getHireDate() );
        employee.setHourlyRate( employeeDTO.getHourlyRate() );
        employee.setFullTime( employeeDTO.getFullTime() );
        employee.setMaxHoursPerWeek( employeeDTO.getMaxHoursPerWeek() );
        employee.setNote( employeeDTO.getNote() );

        employee.setStatus( "ACTIVE" );
        employee.setSubordinates( employee.getSubordinates() );
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

    private String employeeManagerFullName(Employee employee) {
        if ( employee == null ) {
            return null;
        }
        Employee manager = employee.getManager();
        if ( manager == null ) {
            return null;
        }
        String fullName = manager.getFullName();
        if ( fullName == null ) {
            return null;
        }
        return fullName;
    }

    protected Location addressDTOToLocation(AddressDTO addressDTO) {
        if ( addressDTO == null ) {
            return null;
        }

        Location location = new Location();

        location.setId( addressDTO.getId() );
        location.setCity( addressDTO.getCity() );
        location.setState( addressDTO.getState() );
        location.setZipCode( addressDTO.getZipCode() );
        location.setCountry( addressDTO.getCountry() );

        return location;
    }

    protected void addressDTOToLocation1(AddressDTO addressDTO, Location mappingTarget) {
        if ( addressDTO == null ) {
            return;
        }

        mappingTarget.setId( addressDTO.getId() );
        mappingTarget.setCity( addressDTO.getCity() );
        mappingTarget.setState( addressDTO.getState() );
        mappingTarget.setZipCode( addressDTO.getZipCode() );
        mappingTarget.setCountry( addressDTO.getCountry() );
    }
}
