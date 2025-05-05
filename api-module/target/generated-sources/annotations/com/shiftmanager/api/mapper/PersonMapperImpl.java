package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.PersonDTO;
import com.shiftmanager.api.model.Person;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-05T22:02:40+0000",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (N/A)"
)
@Component
public class PersonMapperImpl implements PersonMapper {

    @Override
    public PersonDTO toDto(Person person) {
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
    public Person toEntity(PersonDTO personDTO) {
        if ( personDTO == null ) {
            return null;
        }

        Person person = new Person();

        person.setId( personDTO.getId() );
        person.setFirstName( personDTO.getFirstName() );
        person.setLastName( personDTO.getLastName() );
        person.setEmail( personDTO.getEmail() );
        person.setPhoneNumber( personDTO.getPhoneNumber() );

        return person;
    }

    @Override
    public Person updateEntityFromDto(PersonDTO personDTO, Person person) {
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
