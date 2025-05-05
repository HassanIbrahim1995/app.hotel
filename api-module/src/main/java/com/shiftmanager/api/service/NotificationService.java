package com.shiftmanager.api.service;

import com.shiftmanager.api.dto.NotificationDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing notifications
 */
public interface NotificationService {
    
    /**
     * Get all notifications for an employee
     * @param employeeId Employee ID
     * @return List of notification DTOs
     */
    List<NotificationDTO> getNotificationsForEmployee(Long employeeId);
    
    /**
     * Get unread notifications for an employee
     * @param employeeId Employee ID
     * @return List of unread notification DTOs
     */
    List<NotificationDTO> getUnreadNotificationsForEmployee(Long employeeId);
    
    /**
     * Mark a notification as read
     * @param notificationId Notification ID
     * @return Updated notification DTO
     */
    NotificationDTO markNotificationAsRead(Long notificationId);
    
    /**
     * Create a notification for an employee
     * @param employeeId Employee ID
     * @param message Notification message
     * @param type Notification type (e.g., SHIFT_ASSIGNMENT, VACATION_APPROVAL)
     * @param referenceId Optional reference ID (e.g., shift ID, vacation request ID)
     * @return Created notification DTO
     */
    NotificationDTO createNotification(Long employeeId, String message, String type, Long referenceId);
    
    /**
     * Delete a notification
     * @param notificationId Notification ID
     */
    void deleteNotification(Long notificationId);
    
    /**
     * Delete all notifications for an employee
     * @param employeeId Employee ID
     */
    void deleteAllNotificationsForEmployee(Long employeeId);
    
    /**
     * Send email notification
     * @param email Recipient email
     * @param subject Email subject
     * @param content Email content
     * @return True if email was sent successfully
     */
    boolean sendEmailNotification(String email, String subject, String content);
    
    /**
     * Send shift assignment notification
     * @param employeeId Employee ID
     * @param shiftId Shift ID
     * @return Created notification DTO
     */
    NotificationDTO sendShiftAssignmentNotification(Long employeeId, Long shiftId);
    
    /**
     * Send vacation request status notification
     * @param employeeId Employee ID
     * @param vacationRequestId Vacation request ID
     * @param approved Whether the request was approved
     * @return Created notification DTO
     */
    NotificationDTO sendVacationRequestStatusNotification(Long employeeId, Long vacationRequestId, boolean approved);
}