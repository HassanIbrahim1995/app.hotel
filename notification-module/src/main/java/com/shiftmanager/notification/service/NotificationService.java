package com.shiftmanager.notification.service;

import com.shiftmanager.notification.dto.NotificationDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service interface for Notification operations
 */
public interface NotificationService {

    /**
     * Create a notification for an employee
     * @param employeeId The employee ID
     * @param title The notification title
     * @param message The notification message
     * @return The created notification
     */
    NotificationDTO createNotification(Long employeeId, String title, String message);

    /**
     * Get a notification by ID
     * @param id The notification ID
     * @return The notification data
     */
    NotificationDTO getNotificationById(Long id);

    /**
     * Get all notifications
     * @return List of all notifications
     */
    List<NotificationDTO> getAllNotifications();

    /**
     * Get notifications by employee
     * @param employeeId The employee ID
     * @return List of notifications for the specified employee
     */
    List<NotificationDTO> getNotificationsByEmployee(Long employeeId);

    /**
     * Get unread notifications by employee
     * @param employeeId The employee ID
     * @return List of unread notifications for the specified employee
     */
    List<NotificationDTO> getUnreadNotificationsByEmployee(Long employeeId);

    /**
     * Get the count of unread notifications for an employee
     * @param employeeId The employee ID
     * @return The count of unread notifications
     */
    Long getUnreadNotificationCountForEmployee(Long employeeId);

    /**
     * Mark a notification as read
     * @param id The notification ID
     * @return The updated notification
     */
    NotificationDTO markNotificationAsRead(Long id);

    /**
     * Mark all notifications as read for an employee
     * @param employeeId The employee ID
     * @return The count of notifications marked as read
     */
    Long markAllNotificationsAsReadForEmployee(Long employeeId);

    /**
     * Delete a notification
     * @param id The notification ID
     */
    void deleteNotification(Long id);

    /**
     * Process email notifications that have not been sent yet
     * @return The count of notifications processed
     */
    Long processEmailNotifications();
}
