package com.shiftmanager.notification.service.impl;

import com.shiftmanager.core.domain.Employee;
import com.shiftmanager.core.domain.Notification;
import com.shiftmanager.core.repository.EmployeeRepository;
import com.shiftmanager.core.repository.NotificationRepository;
import com.shiftmanager.notification.dto.NotificationDTO;
import com.shiftmanager.notification.service.EmailService;
import com.shiftmanager.notification.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of NotificationService
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public NotificationDTO createNotification(Long employeeId, String title, String message) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + employeeId));
        
        Notification notification = Notification.builder()
                .employee(employee)
                .title(title)
                .message(message)
                .read(false)
                .emailSent(false)
                .build();
        
        Notification savedNotification = notificationRepository.save(notification);
        return mapEntityToDto(savedNotification);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
        return mapEntityToDto(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotificationsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        return notificationRepository.findByEmployeeId(employeeId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotificationsByEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        return notificationRepository.findByEmployeeIdAndReadFalse(employeeId).stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getUnreadNotificationCountForEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        return notificationRepository.countByEmployeeIdAndReadFalse(employeeId);
    }

    @Override
    @Transactional
    public NotificationDTO markNotificationAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found with id: " + id));
        
        notification.markAsRead();
        Notification updatedNotification = notificationRepository.save(notification);
        
        return mapEntityToDto(updatedNotification);
    }

    @Override
    @Transactional
    public Long markAllNotificationsAsReadForEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
        
        List<Notification> unreadNotifications = notificationRepository.findByEmployeeIdAndReadFalse(employeeId);
        
        for (Notification notification : unreadNotifications) {
            notification.markAsRead();
            notificationRepository.save(notification);
        }
        
        return (long) unreadNotifications.size();
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new EntityNotFoundException("Notification not found with id: " + id);
        }
        
        notificationRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long processEmailNotifications() {
        List<Notification> unsent = notificationRepository.findByEmailSentFalse();
        
        long count = 0;
        for (Notification notification : unsent) {
            try {
                // Send email
                emailService.sendNotificationEmail(
                        notification.getEmployee().getEmail(),
                        notification.getTitle(),
                        notification.getMessage());
                
                // Mark as sent
                notification.markEmailSent();
                notificationRepository.save(notification);
                count++;
            } catch (Exception e) {
                // Log the error but continue processing others
                // In a real system, you might want more sophisticated error handling/retry logic
                System.err.println("Failed to send email notification ID " + notification.getId() + ": " + e.getMessage());
            }
        }
        
        return count;
    }

    /**
     * Maps Notification entity to NotificationDTO
     * @param notification The entity to map
     * @return The mapped DTO
     */
    private NotificationDTO mapEntityToDto(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .employeeId(notification.getEmployee().getId())
                .employeeName(notification.getEmployee().getName().getFullName())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.getRead())
                .emailSent(notification.getEmailSent())
                .readAt(notification.getReadAt())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}
