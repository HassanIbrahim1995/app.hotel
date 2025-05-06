package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.PersonDTO;
import com.shiftmanager.api.model.Employee;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-06T01:20:51+0200",
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

        employee.setId( personDTO.getId() );
        employee.setFirstName( personDTO.getFirstName() );
        employee.setLastName( personDTO.getLastName() );
        employee.setEmail( personDTO.getEmail() );
        employee.setPhoneNumber( personDTO.getPhoneNumber() );

        return employee;
    }

    @Override
    public Employee updateEntityFromDto(PersonDTO personDTO, Employee person) {
        if ( personDTO == null ) {
            return person;
        }

        person.setId( personDTO.getId() );
        person.setFirstName( personDTO.getFirstName() );
        person.setLastName( personDTO.getLastName() );
        person.setEmail( personDTO.getEmail() );
        person.setPhoneNumber( personDTO.getPhoneNumber() );

        return person;
    }
}
