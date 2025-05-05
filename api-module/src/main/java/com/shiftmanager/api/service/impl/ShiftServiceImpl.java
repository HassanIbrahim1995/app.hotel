package com.shiftmanager.api.service.impl;

import com.shiftmanager.api.dto.EmployeeShiftDTO;
import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.mapper.EmployeeShiftMapper;
import com.shiftmanager.api.mapper.ShiftMapper;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.EmployeeShiftRepository;
import com.shiftmanager.api.repository.LocationRepository;
import com.shiftmanager.api.repository.ShiftRepository;
import com.shiftmanager.api.service.NotificationService;
import com.shiftmanager.api.service.ShiftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the ShiftService interface
 */
@Service
@Transactional
public class ShiftServiceImpl implements ShiftService {
    
    private static final Logger logger = LoggerFactory.getLogger(ShiftServiceImpl.class);
    
    @Autowired
    private ShiftRepository shiftRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeShiftRepository employeeShiftRepository;
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private ShiftMapper shiftMapper;
    
    @Autowired
    private EmployeeShiftMapper employeeShiftMapper;
    
    @Autowired
    private NotificationService notificationService;
    
    @Override
    public List<ShiftDTO> getAllShifts() {
        logger.debug("Getting all shifts");
        return shiftRepository.findAll().stream()
                .map(shiftMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<ShiftDTO> getShiftById(Long id) {
        logger.debug("Getting shift with ID: {}", id);
        return shiftRepository.findById(id)
                .map(shiftMapper::toDto);
    }
    
    @Override
    public ShiftDTO createShift(ShiftDTO shiftDTO) {
        logger.debug("Creating new shift: {}", shiftDTO);
        
        Shift shift = shiftMapper.toEntity(shiftDTO);
        Shift savedShift = shiftRepository.save(shift);
        return shiftMapper.toDto(savedShift);
    }
    
    @Override
    public ShiftDTO updateShift(Long id, ShiftDTO shiftDTO) {
        logger.debug("Updating shift with ID: {}", id);
        
        return shiftRepository.findById(id)
                .map(shift -> {
                    shiftMapper.updateEntityFromDto(shiftDTO, shift);
                    Shift updatedShift = shiftRepository.save(shift);
                    return shiftMapper.toDto(updatedShift);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + id));
    }
    
    @Override
    public void deleteShift(Long id) {
        logger.debug("Deleting shift with ID: {}", id);
        
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + id));
        
        // Remove employee assignments first
        shift.getEmployeeShifts().clear();
        shiftRepository.save(shift);
        
        // Now delete the shift
        shiftRepository.delete(shift);
    }
    
    @Override
    public List<ShiftDTO> getShiftsByDateRange(LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting shifts from {} to {}", startDate, endDate);
        
        return shiftRepository.findByShiftDateBetween(startDate, endDate).stream()
                .map(shiftMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ShiftDTO> getShiftsByLocation(Long locationId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting shifts for location ID: {} from {} to {}", locationId, startDate, endDate);
        
        return locationRepository.findById(locationId)
                .map(location -> shiftRepository.findByLocationAndShiftDateBetween(location, startDate, endDate).stream()
                        .map(shiftMapper::toDto)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("Location not found with ID: " + locationId));
    }
    
    @Override
    public EmployeeShiftDTO assignEmployeeToShift(Long shiftId, Long employeeId) {
        logger.debug("Assigning employee ID: {} to shift ID: {}", employeeId, shiftId);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + shiftId));
        
        // Check if assignment already exists
        Optional<EmployeeShift> existingAssignment = employeeShiftRepository.findByEmployeeAndShift(employee, shift);
        if (existingAssignment.isPresent()) {
            return employeeShiftMapper.toDto(existingAssignment.get());
        }
        
        // Create new assignment
        EmployeeShift employeeShift = new EmployeeShift(employee, shift, "ASSIGNED");
        EmployeeShift savedEmployeeShift = employeeShiftRepository.save(employeeShift);
        
        // Send notification to the employee
        notificationService.sendShiftAssignmentNotification(employeeId, shiftId);
        
        return employeeShiftMapper.toDto(savedEmployeeShift);
    }
    
    @Override
    public void removeEmployeeFromShift(Long shiftId, Long employeeId) {
        logger.debug("Removing employee ID: {} from shift ID: {}", employeeId, shiftId);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + shiftId));
        
        employeeShiftRepository.findByEmployeeAndShift(employee, shift)
                .ifPresent(employeeShift -> {
                    employeeShiftRepository.delete(employeeShift);
                    
                    // Send notification to the employee
                    notificationService.createNotification(
                            employeeId,
                            "You have been removed from a shift on " + shift.getShiftDate(),
                            "SHIFT_REMOVAL",
                            shiftId);
                });
    }
    
    @Override
    public EmployeeShiftDTO updateShiftStatus(Long employeeShiftId, String status) {
        logger.debug("Updating shift status to {} for employee shift ID: {}", status, employeeShiftId);
        
        EmployeeShift employeeShift = employeeShiftRepository.findById(employeeShiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee shift not found with ID: " + employeeShiftId));
        
        employeeShift.setStatus(status);
        EmployeeShift savedEmployeeShift = employeeShiftRepository.save(employeeShift);
        
        return employeeShiftMapper.toDto(savedEmployeeShift);
    }
    
    @Override
    public byte[] generateScheduleReport(Long locationId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Generating schedule report for location ID: {} from {} to {}", locationId, startDate, endDate);
        
        // This would typically integrate with a PDF generation library like iText or JasperReports
        // For now, we'll return a placeholder
        return new byte[0]; // Placeholder implementation
    }
}