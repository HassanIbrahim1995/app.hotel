package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.PersonDTO;
import com.shiftmanager.api.model.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-11T01:56:19+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class PersonMapperImpl implements PersonMapper {

    @Override
    public PersonDTO toDto(Employee person) {
        if ( person == null ) {
            return null;
        }

        PersonDTO.PersonDTOBuilder<?, ?> personDTO = PersonDTO.builder();

        personDTO.id( person.getId() );
        personDTO.firstName( person.getFirstName() );
        personDTO.lastName( person.getLastName() );
        personDTO.email( person.getEmail() );
        personDTO.phoneNumber( person.getPhoneNumber() );

        return personDTO.build();
    }

    @Override
    public Employee toEntity(PersonDTO personDTO) {
        if ( personDTO == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setAddress( idToAddress( personDTO.getId() ) );
        if ( personDTO.getId() != null ) {
            employee.setEmployeeId( String.valueOf( personDTO.getId() ) );
        }
        employee.setJobTitle( personDTO.getEmail() );
        employee.setEmployeeNumber( personDTO.getPhoneNumber() );
        if ( personDTO.getId() != null ) {
            employee.setPosition( String.valueOf( personDTO.getId() ) );
        }
        employee.setDepartment( personDTO.getLastName() );
        if ( personDTO.getId() != null ) {
            employee.setNote( String.valueOf( personDTO.getId() ) );
        }
        employee.setId( personDTO.getId() );
        employee.setFirstName( personDTO.getFirstName() );
        employee.setLastName( personDTO.getLastName() );
        employee.setEmail( personDTO.getEmail() );
        employee.setPhoneNumber( personDTO.getPhoneNumber() );

        employee.setHireDate( java.time.LocalDate.now() );
        employee.setHourlyRate( (double) 0.0 );
        employee.setFullTime( true );
        employee.setMaxHoursPerWeek( 40 );
        employee.setStatus( "ACTIVE" );
        employee.setSubordinates( new java.util.ArrayList<>() );

        return employee;
    }

    @Override
    public Employee updateEntityFromDto(PersonDTO personDTO, Employee person) {
        if ( personDTO == null ) {
            return person;
        }

        person.setAddress( idToAddress( personDTO.getId() ) );
        if ( personDTO.getId() != null ) {
            person.setEmployeeId( String.valueOf( personDTO.getId() ) );
        }
        else {
            person.setEmployeeId( null );
        }
        person.setJobTitle( personDTO.getEmail() );
        person.setEmployeeNumber( personDTO.getPhoneNumber() );
        if ( personDTO.getId() != null ) {
            person.setPosition( String.valueOf( personDTO.getId() ) );
        }
        else {
            person.setPosition( null );
        }
        person.setDepartment( personDTO.getLastName() );
        if ( personDTO.getId() != null ) {
            person.setNote( String.valueOf( personDTO.getId() ) );
        }
        else {
            person.setNote( null );
        }
        person.setId( personDTO.getId() );
        person.setFirstName( personDTO.getFirstName() );
        person.setLastName( personDTO.getLastName() );
        person.setEmail( personDTO.getEmail() );
        person.setPhoneNumber( personDTO.getPhoneNumber() );

        person.setHireDate( java.time.LocalDate.now() );
        person.setHourlyRate( (double) 0.0 );
        person.setFullTime( true );
        person.setMaxHoursPerWeek( 40 );
        person.setStatus( "ACTIVE" );
        person.setSubordinates( person.getSubordinates() );

        return person;
    }
}
