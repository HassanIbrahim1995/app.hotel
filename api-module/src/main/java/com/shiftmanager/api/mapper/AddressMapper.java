package com.shiftmanager.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.shiftmanager.api.dto.AddressDTO;
import com.shiftmanager.api.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    
    AddressDTO toDTO(Address address);
    
    Address toEntity(AddressDTO addressDTO);
    
    void updateEntityFromDTO(AddressDTO addressDTO, @MappingTarget Address address);
}
