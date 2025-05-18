package com.shiftmanager.api.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shiftmanager.api.dto.EmployeeDTO;
import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.mapper.EmployeeMapper;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.EmployeeShift;
import com.shiftmanager.api.model.Notification;
import com.shiftmanager.api.model.VacationRequest;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.EmployeeShiftRepository;
import com.shiftmanager.api.repository.NotificationRepository;
import com.shiftmanager.api.repository.VacationRequestRepository;
import com.shiftmanager.api.service.EmployeeService;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final VacationRequestRepository vacationRequestRepository;
    private final NotificationRepository notificationRepository;
    private final EmployeeShiftRepository employeeShiftRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        return employeeMapper.toDTO(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO getCurrentEmployee() {
        String username = getCurrentUsername();
        Employee employee = employeeRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + username));
        return employeeMapper.toDTO(employee);
    }

    @Override
    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Check if employee with same email or employee number already exists
        employeeRepository.findByEmail(employeeDTO.getEmail())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Employee with email " + employeeDTO.getEmail() + " already exists");
                });
        
        employeeRepository.findByEmployeeNumber(employeeDTO.getEmployeeNumber())
                .ifPresent(e -> {
                    throw new IllegalArgumentException("Employee with number " + employeeDTO.getEmployeeNumber() + " already exists");
                });
        
        // Set manager if managerId is provided
        Employee employee = employeeMapper.toEntity(employeeDTO);
        if (employeeDTO.getManagerId() != null) {
            Employee manager = employeeRepository.findById(employeeDTO.getManagerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + employeeDTO.getManagerId()));
            employee.setManager(manager);
        }
        
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
        
        // Check email uniqueness if changed
        if (!existingEmployee.getEmail().equals(employeeDTO.getEmail())) {
            employeeRepository.findByEmail(employeeDTO.getEmail())
                    .ifPresent(e -> {
                        throw new IllegalArgumentException("Employee with email " + employeeDTO.getEmail() + " already exists");
                    });
        }
        
        // Check employee number uniqueness if changed
        if (!existingEmployee.getEmployeeNumber().equals(employeeDTO.getEmployeeNumber())) {
            employeeRepository.findByEmployeeNumber(employeeDTO.getEmployeeNumber())
                    .ifPresent(e -> {
                        throw new IllegalArgumentException("Employee with number " + employeeDTO.getEmployeeNumber() + " already exists");
                    });
        }
        
        // Update basic fields
        employeeMapper.updateEntityFromDTO(employeeDTO, existingEmployee);
        
        // Set manager if managerId is provided and changed
        if (employeeDTO.getManagerId() != null) {
            if (existingEmployee.getManager() == null || !existingEmployee.getManager().getId().equals(employeeDTO.getManagerId())) {
                Employee manager = employeeRepository.findById(employeeDTO.getManagerId())
                        .orElseThrow(() -> new ResourceNotFoundException("Manager not found with id: " + employeeDTO.getManagerId()));
                existingEmployee.setManager(manager);
            }
        } else {
            existingEmployee.setManager(null);
        }
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return employeeMapper.toDTO(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> getEmployeesByManager(Long managerId) {
        return employeeRepository.findByManagerId(managerId)
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> searchEmployees(String query) {
        return employeeRepository.searchEmployees(query)
                .stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        }
        return principal.toString();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Employee getEmployeeEntity(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<VacationRequest> getEmployeeVacationRequests(Long employeeId) {
        Employee employee = getEmployeeEntity(employeeId);
        return vacationRequestRepository.findByEmployee(employee);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<VacationRequest> getEmployeeVacationRequestsByStatus(Long employeeId, String status) {
        Employee employee = getEmployeeEntity(employeeId);
        if (status == null || status.isEmpty()) {
            return vacationRequestRepository.findByEmployee(employee);
        }
        return vacationRequestRepository.findByEmployeeAndStatus(employee, status);
    }
    
    @Override
    @Transactional
    public VacationRequest createVacationRequest(Long employeeId, VacationRequest vacationRequest) {
        Employee employee = getEmployeeEntity(employeeId);
        
        // Check for conflicts
        if (hasVacationConflicts(employeeId, vacationRequest.getStartDate(), vacationRequest.getEndDate(), null)) {
            throw new IllegalArgumentException("Vacation request conflicts with existing vacation requests");
        }
        
        // Set employee and default values
        vacationRequest.setEmployee(employee);
        vacationRequest.setRequestDate(LocalDate.now());
        vacationRequest.setStatus("PENDING");
        
        return vacationRequestRepository.save(vacationRequest);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean hasVacationConflicts(Long employeeId, LocalDate startDate, LocalDate endDate, Long requestId) {
        Employee employee = getEmployeeEntity(employeeId);
        
        // Get all approved or pending vacation requests for the employee
        List<VacationRequest> existingRequests = vacationRequestRepository.findByEmployeeAndStatusIn(
                employee, List.of("APPROVED", "PENDING"));
        
        // Filter out the request being updated if requestId is provided
        if (requestId != null) {
            existingRequests = existingRequests.stream()
                    .filter(request -> !request.getId().equals(requestId))
                    .collect(Collectors.toList());
        }
        
        // Check for overlaps
        return existingRequests.stream().anyMatch(request -> {
            // Check if date ranges overlap
            return (startDate.isBefore(request.getEndDate()) || startDate.isEqual(request.getEndDate())) &&
                   (endDate.isAfter(request.getStartDate()) || endDate.isEqual(request.getStartDate()));
        });
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Notification> getEmployeeNotifications(Long employeeId, boolean unreadOnly) {
        Employee employee = getEmployeeEntity(employeeId);
        
        if (unreadOnly) {
            return notificationRepository.findByEmployeeAndReadFalseOrderByCreatedAtDesc(employee);
        } else {
            return notificationRepository.findByEmployeeOrderByCreatedAtDesc(employee);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public int getUnreadNotificationsCount(Long employeeId) {
        Employee employee = getEmployeeEntity(employeeId);
        return notificationRepository.countByEmployeeAndReadFalse(employee);
    }
    
    @Override
    @Transactional
    public void markNotificationAsRead(Long employeeId, Long notificationId) {
        Employee employee = getEmployeeEntity(employeeId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));
        
        // Verify the notification belongs to the employee
        if (!notification.getEmployee().getId().equals(employeeId)) {
            throw new IllegalArgumentException("Notification does not belong to the specified employee");
        }
        
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }
    
    @Override
    @Transactional
    public void markAllNotificationsAsRead(Long employeeId) {
        Employee employee = getEmployeeEntity(employeeId);
        
        List<Notification> unreadNotifications = notificationRepository.findByEmployeeAndReadFalseOrderByCreatedAtDesc(employee);
        
        LocalDateTime now = LocalDateTime.now();
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notification.setReadAt(now);
        });
        
        notificationRepository.saveAll(unreadNotifications);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getEmployeeStatistics(Long employeeId, Integer year, Integer month) {
        Employee employee = getEmployeeEntity(employeeId);
        
        Map<String, Object> statistics = new HashMap<>();
        
        // Get date range
        LocalDate startDate, endDate;
        if (month != null) {
            // Monthly statistics
            startDate = LocalDate.of(year, month, 1);
            endDate = startDate.plusMonths(1).minusDays(1);
            statistics.put("period", "Month " + month + ", " + year);
        } else {
            // Yearly statistics
            startDate = LocalDate.of(year, 1, 1);
            endDate = LocalDate.of(year, 12, 31);
            statistics.put("period", "Year " + year);
        }
        
        // Total shifts worked
        List<EmployeeShift> completedShifts = employeeShiftRepository.findByEmployeeAndStatusAndShiftDateBetween(
                employee, "COMPLETED", startDate, endDate);
        
        int totalShifts = completedShifts.size();
        statistics.put("totalShifts", totalShifts);
        
        // Total hours worked
        double totalHours = completedShifts.stream()
                .mapToDouble(shift -> {
                    if (shift.getClockInTime() != null && shift.getClockOutTime() != null) {
                        return (shift.getClockOutTime().toSecondOfDay() - shift.getClockInTime().toSecondOfDay()) / 3600.0;
                    }
                    return 0.0;
                })
                .sum();
        statistics.put("totalHours", Math.round(totalHours * 100.0) / 100.0);
        
        // Vacation days
        List<VacationRequest> approvedVacations = vacationRequestRepository.findByEmployeeAndStatusAndDateRange(
                employee, "APPROVED", startDate, endDate);
        
        int vacationDays = approvedVacations.stream()
                .mapToInt(vacation -> {
                    // Count days in the vacation that fall within the date range
                    LocalDate vacStart = vacation.getStartDate().isBefore(startDate) ? startDate : vacation.getStartDate();
                    LocalDate vacEnd = vacation.getEndDate().isAfter(endDate) ? endDate : vacation.getEndDate();
                    return (int) (vacEnd.toEpochDay() - vacStart.toEpochDay() + 1);
                })
                .sum();
        statistics.put("vacationDays", vacationDays);
        
        // Pending vacation requests
        long pendingVacations = vacationRequestRepository.countByEmployeeAndStatus(employee, "PENDING");
        statistics.put("pendingVacationRequests", pendingVacations);
        
        // Upcoming shifts
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        long upcomingShifts = employeeShiftRepository.countByEmployeeAndShiftDateBetween(
                employee, today, nextWeek);
        statistics.put("upcomingShifts", upcomingShifts);
        
        return statistics;
    }
}
