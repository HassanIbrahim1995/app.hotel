package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.PersonDTO;
import com.shiftmanager.api.model.Person;
import org.mapstruct.*;

/**
 * Mapper for the Person entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {
    
    @Override
    @Mapping(target = "fullName", ignore = true)
    PersonDTO toDto(Person person);
    
    @Override
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Person toEntity(PersonDTO personDTO);
    
    @Override
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Person updateEntityFromDto(PersonDTO personDTO, @MappingTarget Person person);
    
    /**
     * Generate the full name from first and last name
     * @param person The person entity
     * @param dto The DTO to populate
     */
    @AfterMapping
    default void setFullName(Person person, @MappingTarget PersonDTO dto) {
        if (person != null) {
            dto.setFullName(person.getFirstName() + " " + person.getLastName());
        }
    }
}
