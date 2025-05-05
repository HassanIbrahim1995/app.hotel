package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.PersonDTO;
import com.shiftmanager.api.model.Employee;
import org.mapstruct.*;

/**
 * Mapper for the Person entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface PersonMapper {
    
    @Mapping(target = "fullName", ignore = true)
    PersonDTO toDto(Employee person);
    
    @Mapping(target = "manager", ignore = true)
    Employee toEntity(PersonDTO personDTO);
    
    @Mapping(target = "manager", ignore = true)
    Employee updateEntityFromDto(PersonDTO personDTO, @MappingTarget Employee person);
    
    /**
     * Generate the full name from first and last name
     * @param person The person entity
     * @param dto The DTO to populate
     */
    @AfterMapping
    default void setFullName(Employee person, @MappingTarget PersonDTO dto) {
        if (person != null) {
            dto.setFullName(person.getFirstName() + " " + person.getLastName());
        }
    }
}
