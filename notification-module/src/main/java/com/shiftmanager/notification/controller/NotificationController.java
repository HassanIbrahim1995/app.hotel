package com.shiftmanager.notification.controller;

import com.shiftmanager.notification.dto.NotificationDTO;
import com.shiftmanager.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for notification operations
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Create a notification
     * @param notificationDTO The notification data
     * @return The created notification
     */
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@Valid @RequestBody NotificationDTO notificationDTO) {
        NotificationDTO createdNotification = notificationService.createNotification(
                notificationDTO.getEmployeeId(),
                notificationDTO.getTitle(),
                notificationDTO.getMessage());
        return new ResponseEntity<>(createdNotification, HttpStatus.CREATED);
    }

    /**
     * Get a notification by ID
     * @param id The notification ID
     * @return The notification data
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    /**
     * Get all notifications
     * @return List of all notifications
     */
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    /**
     * Get notifications by employee
     * @param employeeId The employee ID
     * @return List of notifications for the specified employee
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(notificationService.getNotificationsByEmployee(employeeId));
    }

    /**
     * Get unread notifications by employee
     * @param employeeId The employee ID
     * @return List of unread notifications for the specified employee
     */
    @GetMapping("/employee/{employeeId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotificationsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByEmployee(employeeId));
    }

    /**
     * Get the count of unread notifications for an employee
     * @param employeeId The employee ID
     * @return The count of unread notifications
     */
    @GetMapping("/employee/{employeeId}/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadNotificationCountForEmployee(@PathVariable Long employeeId) {
        Long count = notificationService.getUnreadNotificationCountForEmployee(employeeId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * Mark a notification as read
     * @param id The notification ID
     * @return The updated notification
     */
    @PostMapping("/{id}/read")
    public ResponseEntity<NotificationDTO> markNotificationAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.markNotificationAsRead(id));
    }

    /**
     * Mark all notifications as read for an employee
     * @param employeeId The employee ID
     * @return The count of notifications marked as read
     */
    @PostMapping("/employee/{employeeId}/read-all")
    public ResponseEntity<Map<String, Long>> markAllNotificationsAsReadForEmployee(@PathVariable Long employeeId) {
        Long count = notificationService.markAllNotificationsAsReadForEmployee(employeeId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * Delete a notification
     * @param id The notification ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Process email notifications that have not been sent yet
     * @return The count of notifications processed
     */
    @PostMapping("/process-emails")
    public ResponseEntity<Map<String, Long>> processEmailNotifications() {
        Long count = notificationService.processEmailNotifications();
        return ResponseEntity.ok(Map.of("processed", count));
    }
}
