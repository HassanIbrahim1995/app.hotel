package com.shiftmanager.employeemanagement.service;

import com.shiftmanager.employeemanagement.model.Employee;
import com.shiftmanager.employeemanagement.model.Manager;
import com.shiftmanager.employeemanagement.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the EmployeeService interface
 */
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with employee id: " + employeeId));
    }

    @Override
    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);
        
        // Update employee fields
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());
        employee.setPhoneNumber(employeeDetails.getPhoneNumber());
        employee.setAddressLine1(employeeDetails.getAddressLine1());
        employee.setAddressLine2(employeeDetails.getAddressLine2());
        employee.setCity(employeeDetails.getCity());
        employee.setState(employeeDetails.getState());
        employee.setPostalCode(employeeDetails.getPostalCode());
        employee.setCountry(employeeDetails.getCountry());
        employee.setEmergencyContactName(employeeDetails.getEmergencyContactName());
        employee.setEmergencyContactPhone(employeeDetails.getEmergencyContactPhone());
        
        // Update employee-specific fields
        employee.setJobTitle(employeeDetails.getJobTitle());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setEmploymentType(employeeDetails.getEmploymentType());
        employee.setEmploymentStatus(employeeDetails.getEmploymentStatus());
        employee.setHourlyRate(employeeDetails.getHourlyRate());
        employee.setWeeklyHours(employeeDetails.getWeeklyHours());
        employee.setVacationDays(employeeDetails.getVacationDays());
        employee.setSickDays(employeeDetails.getSickDays());
        employee.setNotes(employeeDetails.getNotes());
        employee.setSpecialRequirements(employeeDetails.getSpecialRequirements());
        
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> getEmployeesByManager(Long managerId) {
        return employeeRepository.findByManagerId(managerId);
    }

    @Override
    @Transactional
    public Employee assignManager(Long employeeId, Manager manager) {
        Employee employee = getEmployeeById(employeeId);
        employee.setManager(manager);
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmploymentStatus(Long employeeId, Employee.EmploymentStatus status) {
        Employee employee = getEmployeeById(employeeId);
        employee.setEmploymentStatus(status);
        
        // If terminating, set the terminated date
        if (status == Employee.EmploymentStatus.TERMINATED) {
            employee.setTerminatedDate(LocalDate.now());
        }
        
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employee> searchEmployees(String department, String jobTitle, 
                                      Employee.EmploymentType employmentType,
                                      Employee.EmploymentStatus employmentStatus,
                                      LocalDate hiredAfter) {
        // Simple implementation for now
        List<Employee> result = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        
        for (Employee employee : employees) {
            boolean matches = true;
            
            if (department != null && !department.equals(employee.getDepartment())) {
                matches = false;
            }
            
            if (jobTitle != null && !jobTitle.equals(employee.getJobTitle())) {
                matches = false;
            }
            
            if (employmentType != null && employmentType != employee.getEmploymentType()) {
                matches = false;
            }
            
            if (employmentStatus != null && employmentStatus != employee.getEmploymentStatus()) {
                matches = false;
            }
            
            if (hiredAfter != null && (employee.getHireDate() == null || 
                    employee.getHireDate().isBefore(hiredAfter))) {
                matches = false;
            }
            
            if (matches) {
                result.add(employee);
            }
        }
        
        return result;
    }
}
