package com.shiftmanager.api.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.shiftmanager.api.dto.EmployeeDTO;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Notification;
import com.shiftmanager.api.model.VacationRequest;

public interface EmployeeService {
    
    List<EmployeeDTO> getAllEmployees();
    
    EmployeeDTO getEmployeeById(Long id);
    
    EmployeeDTO getCurrentEmployee();
    
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);
    
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    
    void deleteEmployee(Long id);
    
    List<EmployeeDTO> getEmployeesByManager(Long managerId);
    
    List<EmployeeDTO> searchEmployees(String query);
    
    /**
     * Get vacation requests for an employee
     * @param employeeId Employee ID
     * @return List of vacation requests
     */
    List<VacationRequest> getEmployeeVacationRequests(Long employeeId);
    
    /**
     * Get vacation requests for an employee with the specified status
     * @param employeeId Employee ID
     * @param status Status filter (optional)
     * @return List of vacation requests matching the criteria
     */
    List<VacationRequest> getEmployeeVacationRequestsByStatus(Long employeeId, String status);
    
    /**
     * Create a new vacation request for an employee
     * @param employeeId Employee ID
     * @param vacationRequest Vacation request data
     * @return Created vacation request
     */
    VacationRequest createVacationRequest(Long employeeId, VacationRequest vacationRequest);
    
    /**
     * Check if the specified date range has conflicts with existing vacation requests
     * @param employeeId Employee ID
     * @param startDate Start date
     * @param endDate End date
     * @param requestId Vacation request ID to exclude from check (optional)
     * @return true if conflicts exist
     */
    boolean hasVacationConflicts(Long employeeId, LocalDate startDate, LocalDate endDate, Long requestId);
    
    /**
     * Get employee notifications
     * @param employeeId Employee ID
     * @param unreadOnly Only include unread notifications if true
     * @return List of notifications
     */
    List<Notification> getEmployeeNotifications(Long employeeId, boolean unreadOnly);
    
    /**
     * Get count of unread notifications for an employee
     * @param employeeId Employee ID
     * @return Count of unread notifications
     */
    int getUnreadNotificationsCount(Long employeeId);
    
    /**
     * Mark a notification as read
     * @param employeeId Employee ID
     * @param notificationId Notification ID
     */
    void markNotificationAsRead(Long employeeId, Long notificationId);
    
    /**
     * Mark all notifications for an employee as read
     * @param employeeId Employee ID
     */
    void markAllNotificationsAsRead(Long employeeId);
    
    /**
     * Get employee statistics for a year or month
     * @param employeeId Employee ID
     * @param year Year
     * @param month Month (optional)
     * @return Statistics data
     */
    Map<String, Object> getEmployeeStatistics(Long employeeId, Integer year, Integer month);
    
    /**
     * Get employee by ID (model entity)
     * @param id Employee ID
     * @return Employee entity
     */
    Employee getEmployeeEntity(Long id);
}
