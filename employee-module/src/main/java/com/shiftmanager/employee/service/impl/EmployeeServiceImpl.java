package com.shiftmanager.employee.service.impl;

import com.shiftmanager.core.domain.*;
import com.shiftmanager.core.repository.EmployeeRepository;
import com.shiftmanager.core.repository.LocationRepository;
import com.shiftmanager.core.repository.ManagerRepository;
import com.shiftmanager.employee.dto.EmployeeDTO;
import com.shiftmanager.employee.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of EmployeeService
 */
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ManagerRepository managerRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = mapDtoToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapEntityToDto(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        return mapEntityToDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
        
        // Update fields from DTO
        updateEmployeeFromDto(employee, employeeDTO);
        
        Employee updatedEmployee = employeeRepository.save(employee);
        return mapEntityToDto(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByManager(Long managerId) {
        return employeeRepository.findByManagerId(managerId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByLocation(Long locationId) {
        return employeeRepository.findByLocationId(locationId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAvailableEmployeesInDateRange(LocalDate startDate, LocalDate endDate) {
        // Find employees not on approved vacation during the specified date range
        List<Employee> employeesOnVacation = employeeRepository.findEmployeesWithOverlappingVacations(startDate, endDate);
        
        List<Long> employeeIdsOnVacation = employeesOnVacation.stream()
                .map(Employee::getId)
                .collect(Collectors.toList());
        
        return employeeRepository.findAll().stream()
                .filter(employee -> !employeeIdsOnVacation.contains(employee.getId()))
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeByEmployeeNumber(String employeeNumber) {
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNumber)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with employee number: " + employeeNumber));
        return mapEntityToDto(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesWithoutShiftsInRange(LocalDate startDate, LocalDate endDate) {
        return employeeRepository.findEmployeesWithoutShiftsInRange(startDate, endDate).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Maps EmployeeDTO to Employee entity
     * @param dto The DTO to map
     * @return The mapped entity
     */
    private Employee mapDtoToEntity(EmployeeDTO dto) {
        Employee employee = Employee.builder()
                .name(new Name(
                        dto.getName().getFirstName(),
                        dto.getName().getMiddleName(),
                        dto.getName().getLastName()))
                .address(new Address(
                        dto.getAddress().getStreet(),
                        dto.getAddress().getCity(),
                        dto.getAddress().getState(),
                        dto.getAddress().getZipCode(),
                        dto.getAddress().getCountry()))
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .dateOfBirth(dto.getDateOfBirth())
                .nationalId(dto.getNationalId())
                .hireDate(dto.getHireDate())
                .terminationDate(dto.getTerminationDate())
                .employeeNumber(dto.getEmployeeNumber())
                .position(dto.getPosition())
                .department(dto.getDepartment())
                .build();
        
        // Set location if provided
        if (dto.getLocationId() != null) {
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + dto.getLocationId()));
            employee.setLocation(location);
        }
        
        // Set manager if provided
        if (dto.getManagerId() != null) {
            Manager manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + dto.getManagerId()));
            employee.setManager(manager);
        }
        
        return employee;
    }

    /**
     * Maps Employee entity to EmployeeDTO
     * @param employee The entity to map
     * @return The mapped DTO
     */
    private EmployeeDTO mapEntityToDto(Employee employee) {
        EmployeeDTO dto = EmployeeDTO.builder()
                .id(employee.getId())
                .name(new EmployeeDTO.NameDTO(
                        employee.getName().getFirstName(),
                        employee.getName().getMiddleName(),
                        employee.getName().getLastName()))
                .address(new EmployeeDTO.AddressDTO(
                        employee.getAddress().getStreet(),
                        employee.getAddress().getCity(),
                        employee.getAddress().getState(),
                        employee.getAddress().getZipCode(),
                        employee.getAddress().getCountry()))
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .dateOfBirth(employee.getDateOfBirth())
                .nationalId(employee.getNationalId())
                .hireDate(employee.getHireDate())
                .terminationDate(employee.getTerminationDate())
                .employeeNumber(employee.getEmployeeNumber())
                .position(employee.getPosition())
                .department(employee.getDepartment())
                .build();
        
        // Set location if available
        if (employee.getLocation() != null) {
            dto.setLocationId(employee.getLocation().getId());
            dto.setLocationName(employee.getLocation().getName());
        }
        
        // Set manager if available
        if (employee.getManager() != null) {
            dto.setManagerId(employee.getManager().getId());
            
            // Set manager name from their full name
            Name managerName = employee.getManager().getName();
            if (managerName != null) {
                dto.setManagerName(managerName.getFullName());
            }
        }
        
        return dto;
    }

    /**
     * Updates an Employee entity from DTO
     * @param employee The entity to update
     * @param dto The DTO containing updated data
     */
    private void updateEmployeeFromDto(Employee employee, EmployeeDTO dto) {
        if (dto.getName() != null) {
            Name name = employee.getName();
            name.setFirstName(dto.getName().getFirstName());
            name.setMiddleName(dto.getName().getMiddleName());
            name.setLastName(dto.getName().getLastName());
        }
        
        if (dto.getAddress() != null) {
            Address address = employee.getAddress();
            address.setStreet(dto.getAddress().getStreet());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setZipCode(dto.getAddress().getZipCode());
            address.setCountry(dto.getAddress().getCountry());
        }
        
        if (dto.getEmail() != null) {
            employee.setEmail(dto.getEmail());
        }
        
        if (dto.getPhone() != null) {
            employee.setPhone(dto.getPhone());
        }
        
        if (dto.getDateOfBirth() != null) {
            employee.setDateOfBirth(dto.getDateOfBirth());
        }
        
        if (dto.getNationalId() != null) {
            employee.setNationalId(dto.getNationalId());
        }
        
        if (dto.getHireDate() != null) {
            employee.setHireDate(dto.getHireDate());
        }
        
        if (dto.getTerminationDate() != null) {
            employee.setTerminationDate(dto.getTerminationDate());
        }
        
        if (dto.getEmployeeNumber() != null) {
            employee.setEmployeeNumber(dto.getEmployeeNumber());
        }
        
        if (dto.getPosition() != null) {
            employee.setPosition(dto.getPosition());
        }
        
        if (dto.getDepartment() != null) {
            employee.setDepartment(dto.getDepartment());
        }
        
        // Update location if provided
        if (dto.getLocationId() != null) {
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + dto.getLocationId()));
            employee.setLocation(location);
        }
        
        // Update manager if provided
        if (dto.getManagerId() != null) {
            Manager manager = managerRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + dto.getManagerId()));
            employee.setManager(manager);
        }
    }
}
