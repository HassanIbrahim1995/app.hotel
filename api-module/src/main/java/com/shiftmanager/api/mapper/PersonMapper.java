package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.PersonDTO;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Location;
import org.mapstruct.*;
import com.shiftmanager.api.dto.PersonDTO;
import com.shiftmanager.api.model.Address;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Location;
import org.mapstruct.*;

/**
 * Mapper for the Person entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface PersonMapper {

    @Mapping(target = "fullName", ignore = true)
    PersonDTO toDto(Employee person);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "address", source = "id", qualifiedByName = "idToAddress") // Added qualifiedByName
    @Mapping(target = "employeeId", source = "id")
    @Mapping(target = "jobTitle", source = "email")
    @Mapping(target = "employeeNumber", source = "phoneNumber")
    @Mapping(target = "position", source = "id")
    @Mapping(target = "department", source = "lastName")
    @Mapping(target = "hireDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "hourlyRate", constant = "0.0")
    @Mapping(target = "fullTime", constant = "true")
    @Mapping(target = "maxHoursPerWeek", constant = "40")
    @Mapping(target = "note", source = "id")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "subordinates", expression = "java(new java.util.ArrayList<>())")
    Employee toEntity(PersonDTO personDTO);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "address", source = "id", qualifiedByName = "idToAddress") // Added qualifiedByName
    @Mapping(target = "employeeId", source = "id")
    @Mapping(target = "jobTitle", source = "email")
    @Mapping(target = "employeeNumber", source = "phoneNumber")
    @Mapping(target = "position", source = "id")
    @Mapping(target = "department", source = "lastName")
    @Mapping(target = "hireDate", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "hourlyRate", constant = "0.0")
    @Mapping(target = "fullTime", constant = "true")
    @Mapping(target = "maxHoursPerWeek", constant = "40")
    @Mapping(target = "note", source = "id")
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "subordinates", expression = "java(person.getSubordinates())")
    Employee updateEntityFromDto(PersonDTO personDTO, @MappingTarget Employee person);

    /**
     * Convert ID to Address
     * @param id The ID value
     * @return A new Address instance
     */
    @Named("idToAddress")
    default Address idToAddress(Long id) {
        if (id == null) {
            return null;
        }
        // Create a new Address instance with default values
        Address address = new Address();
        // You can set default values or use the ID in some way if needed
        address.setStreet("Default Street");
        address.setCity("Default City");
        address.setState("Default State");
        address.setZipCode("00000");
        address.setCountry("Default Country");
        return address;
    }

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