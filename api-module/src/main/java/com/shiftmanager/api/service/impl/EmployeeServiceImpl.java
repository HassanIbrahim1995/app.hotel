package com.shiftmanager.api.service.impl;

import com.shiftmanager.api.dto.EmployeeDTO;
import com.shiftmanager.api.dto.ShiftDTO;
import com.shiftmanager.api.dto.VacationRequestDTO;
import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.mapper.EmployeeMapper;
import com.shiftmanager.api.mapper.ShiftMapper;
import com.shiftmanager.api.mapper.VacationRequestMapper;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.EmployeeShiftRepository;
import com.shiftmanager.api.repository.VacationRequestRepository;
import com.shiftmanager.api.service.EmployeeService;
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
 * Implementation of the EmployeeService interface
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeShiftRepository employeeShiftRepository;
    
    @Autowired
    private VacationRequestRepository vacationRequestRepository;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Autowired
    private ShiftMapper shiftMapper;
    
    @Autowired
    private VacationRequestMapper vacationRequestMapper;
    
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        logger.debug("Getting all employees");
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        logger.debug("Getting employee with ID: {}", id);
        return employeeRepository.findById(id)
                .map(employeeMapper::toDto);
    }
    
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        logger.debug("Creating new employee: {}", employeeDTO);
        
        // Set default values for new employees
        if (employeeDTO.getHireDate() == null) {
            employeeDTO.setHireDate(LocalDate.now());
        }
        
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setStatus("ACTIVE"); // Default status for new employees
        
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(savedEmployee);
    }
    
    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        logger.debug("Updating employee with ID: {}", id);
        
        return employeeRepository.findById(id)
                .map(employee -> {
                    employeeMapper.updateEntityFromDto(employeeDTO, employee);
                    Employee updatedEmployee = employeeRepository.save(employee);
                    return employeeMapper.toDto(updatedEmployee);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }
    
    @Override
    public void deleteEmployee(Long id) {
        logger.debug("Deleting employee with ID: {}", id);
        
        employeeRepository.findById(id)
                .map(employee -> {
                    // Soft delete - mark as inactive
                    employee.setStatus("INACTIVE");
                    employeeRepository.save(employee);
                    return employee;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
    }
    
    @Override
    public List<ShiftDTO> getEmployeeSchedule(Long employeeId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Getting schedule for employee ID: {} from {} to {}", employeeId, startDate, endDate);
        
        // Find the employee to verify it exists
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        // Get shifts for the employee in the date range
        List<EmployeeShift> employeeShifts = employeeShiftRepository.findByEmployeeAndShiftDateBetween(
                employee, startDate, endDate);
        
        return employeeShifts.stream()
                .map(EmployeeShift::getShift)
                .map(shiftMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<VacationRequestDTO> getEmployeeVacationRequests(Long employeeId) {
        logger.debug("Getting vacation requests for employee ID: {}", employeeId);
        
        // Find the employee to verify it exists
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        return vacationRequestRepository.findByEmployee(employee).stream()
                .map(vacationRequestMapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public byte[] generateEmployeeCalendar(Long employeeId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Generating calendar for employee ID: {} from {} to {}", employeeId, startDate, endDate);
        
        // This would typically integrate with a calendar generation library
        // For now, we'll return a placeholder
        return new byte[0]; // Placeholder implementation
    }
    
    @Override
    public byte[] exportScheduleToPdf(Long employeeId, LocalDate startDate, LocalDate endDate) {
        logger.debug("Exporting schedule to PDF for employee ID: {} from {} to {}", employeeId, startDate, endDate);
        
        // This would typically integrate with a PDF generation library like iText or JasperReports
        // For now, we'll return a placeholder
        return new byte[0]; // Placeholder implementation
    }
}