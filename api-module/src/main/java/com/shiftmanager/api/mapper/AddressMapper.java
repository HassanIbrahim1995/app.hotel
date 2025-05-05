package com.shiftmanager.api.mapper;

import com.shiftmanager.api.dto.AddressDTO;
import com.shiftmanager.api.model.Address;
import com.shiftmanager.api.repository.PersonRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper for the Address entity and its DTO
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class AddressMapper implements EntityMapper<AddressDTO, Address> {
    
    @Autowired
    private PersonRepository personRepository;
    
    @Override
    @Mapping(source = "person.id", target = "personId")
    public abstract AddressDTO toDto(Address address);
    
    @Override
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Address toEntity(AddressDTO addressDTO);
    
    @Override
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    public abstract Address updateEntityFromDto(AddressDTO addressDTO, @MappingTarget Address address);
    
    /**
     * Set the person relation based on the personId in the DTO
     * @param addressDTO The DTO
     * @param address The target entity
     */
    @AfterMapping
    protected void setPerson(AddressDTO addressDTO, @MappingTarget Address address) {
        if (addressDTO.getPersonId() != null) {
            personRepository.findById(addressDTO.getPersonId())
                    .ifPresent(address::setPerson);
        }
    }
}
