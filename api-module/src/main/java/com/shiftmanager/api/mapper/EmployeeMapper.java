package com.shiftmanager.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.shiftmanager.api.dto.EmployeeDTO;
import com.shiftmanager.api.model.Employee;
import org.mapstruct.Named;
import com.shiftmanager.api.model.Location;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerName", source = "manager.fullName")
    EmployeeDTO toDto(Employee employee);

    @Mapping(target = "managerId", source = "manager.id")
    @Mapping(target = "managerName", source = "manager.fullName")
    EmployeeDTO toDTO(Employee employee);

    default java.util.List<EmployeeDTO> toDto(java.util.List<Employee> employees) {
        return employees.stream().map(this::toDto).collect(java.util.stream.Collectors.toList());
    }

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "employeeId", source = "id")
    @Mapping(target = "jobTitle", source = "position") // Fixed: map from position instead
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "location", source = "address") // Fixed: map from address
    @Mapping(target = "subordinates", expression = "java(new java.util.ArrayList<>())")
    Employee toEntity(EmployeeDTO employeeDTO);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "employeeId", source = "id")
    @Mapping(target = "jobTitle", source = "position") // Fixed: map from position instead
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "location", source = "address") // Fixed: map from address
    @Mapping(target = "subordinates", expression = "java(employee.getSubordinates())")
    void updateEntityFromDTO(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}