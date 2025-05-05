package com.shiftmanager.manager.service.impl;

import com.shiftmanager.core.domain.*;
import com.shiftmanager.core.repository.EmployeeRepository;
import com.shiftmanager.core.repository.LocationRepository;
import com.shiftmanager.core.repository.ManagerRepository;
import com.shiftmanager.manager.dto.ManagerDTO;
import com.shiftmanager.manager.service.ManagerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ManagerService
 */
@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;
    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public ManagerDTO createManager(ManagerDTO managerDTO) {
        Manager manager = mapDtoToEntity(managerDTO);
        Manager savedManager = managerRepository.save(manager);
        return mapEntityToDto(savedManager);
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerDTO getManagerById(Long id) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + id));
        return mapEntityToDto(manager);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerDTO> getAllManagers() {
        return managerRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ManagerDTO updateManager(Long id, ManagerDTO managerDTO) {
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + id));
        
        // Update fields from DTO
        updateManagerFromDto(manager, managerDTO);
        
        Manager updatedManager = managerRepository.save(manager);
        return mapEntityToDto(updatedManager);
    }

    @Override
    @Transactional
    public void deleteManager(Long id) {
        if (!managerRepository.existsById(id)) {
            throw new EntityNotFoundException("Manager not found with id: " + id);
        }
        managerRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerDTO> getManagersByDepartment(String department) {
        return managerRepository.findByDepartment(department).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerDTO> getManagersByLevel(String level) {
        return managerRepository.findByManagerLevel(level).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ManagerDTO> getManagersByLocation(Long locationId) {
        return managerRepository.findByLocationId(locationId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ManagerDTO getManagerByManagerNumber(String managerNumber) {
        Manager manager = managerRepository.findByManagerNumber(managerNumber)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with manager number: " + managerNumber));
        return mapEntityToDto(manager);
    }

    @Override
    @Transactional
    public ManagerDTO assignEmployeeToManager(Long managerId, Long employeeId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + managerId));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));
        
        manager.addEmployee(employee);
        Manager updatedManager = managerRepository.save(manager);
        
        return mapEntityToDto(updatedManager);
    }

    @Override
    @Transactional
    public ManagerDTO removeEmployeeFromManager(Long managerId, Long employeeId) {
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new EntityNotFoundException("Manager not found with id: " + managerId));
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));
        
        if (!employee.getManager().getId().equals(managerId)) {
            throw new IllegalStateException("Employee is not managed by this manager");
        }
        
        manager.removeEmployee(employee);
        Manager updatedManager = managerRepository.save(manager);
        
        return mapEntityToDto(updatedManager);
    }

    /**
     * Maps ManagerDTO to Manager entity
     * @param dto The DTO to map
     * @return The mapped entity
     */
    private Manager mapDtoToEntity(ManagerDTO dto) {
        Manager manager = Manager.builder()
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
                .managerNumber(dto.getManagerNumber())
                .managerLevel(dto.getManagerLevel())
                .department(dto.getDepartment())
                .build();
        
        // Set location if provided
        if (dto.getLocationId() != null) {
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + dto.getLocationId()));
            manager.setLocation(location);
        }
        
        return manager;
    }

    /**
     * Maps Manager entity to ManagerDTO
     * @param manager The entity to map
     * @return The mapped DTO
     */
    private ManagerDTO mapEntityToDto(Manager manager) {
        ManagerDTO dto = ManagerDTO.builder()
                .id(manager.getId())
                .name(new ManagerDTO.NameDTO(
                        manager.getName().getFirstName(),
                        manager.getName().getMiddleName(),
                        manager.getName().getLastName()))
                .address(new ManagerDTO.AddressDTO(
                        manager.getAddress().getStreet(),
                        manager.getAddress().getCity(),
                        manager.getAddress().getState(),
                        manager.getAddress().getZipCode(),
                        manager.getAddress().getCountry()))
                .email(manager.getEmail())
                .phone(manager.getPhone())
                .dateOfBirth(manager.getDateOfBirth())
                .nationalId(manager.getNationalId())
                .hireDate(manager.getHireDate())
                .terminationDate(manager.getTerminationDate())
                .managerNumber(manager.getManagerNumber())
                .managerLevel(manager.getManagerLevel())
                .department(manager.getDepartment())
                .employeeCount(manager.getManagedEmployees().size())
                .build();
        
        // Set location if available
        if (manager.getLocation() != null) {
            dto.setLocationId(manager.getLocation().getId());
            dto.setLocationName(manager.getLocation().getName());
        }
        
        return dto;
    }

    /**
     * Updates a Manager entity from DTO
     * @param manager The entity to update
     * @param dto The DTO containing updated data
     */
    private void updateManagerFromDto(Manager manager, ManagerDTO dto) {
        if (dto.getName() != null) {
            Name name = manager.getName();
            name.setFirstName(dto.getName().getFirstName());
            name.setMiddleName(dto.getName().getMiddleName());
            name.setLastName(dto.getName().getLastName());
        }
        
        if (dto.getAddress() != null) {
            Address address = manager.getAddress();
            address.setStreet(dto.getAddress().getStreet());
            address.setCity(dto.getAddress().getCity());
            address.setState(dto.getAddress().getState());
            address.setZipCode(dto.getAddress().getZipCode());
            address.setCountry(dto.getAddress().getCountry());
        }
        
        if (dto.getEmail() != null) {
            manager.setEmail(dto.getEmail());
        }
        
        if (dto.getPhone() != null) {
            manager.setPhone(dto.getPhone());
        }
        
        if (dto.getDateOfBirth() != null) {
            manager.setDateOfBirth(dto.getDateOfBirth());
        }
        
        if (dto.getNationalId() != null) {
            manager.setNationalId(dto.getNationalId());
        }
        
        if (dto.getHireDate() != null) {
            manager.setHireDate(dto.getHireDate());
        }
        
        if (dto.getTerminationDate() != null) {
            manager.setTerminationDate(dto.getTerminationDate());
        }
        
        if (dto.getManagerNumber() != null) {
            manager.setManagerNumber(dto.getManagerNumber());
        }
        
        if (dto.getManagerLevel() != null) {
            manager.setManagerLevel(dto.getManagerLevel());
        }
        
        if (dto.getDepartment() != null) {
            manager.setDepartment(dto.getDepartment());
        }
        
        // Update location if provided
        if (dto.getLocationId() != null) {
            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() -> new EntityNotFoundException("Location not found with id: " + dto.getLocationId()));
            manager.setLocation(location);
        }
    }
}
