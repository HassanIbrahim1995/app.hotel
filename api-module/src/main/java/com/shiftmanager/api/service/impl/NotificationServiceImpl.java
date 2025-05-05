package com.shiftmanager.api.service.impl;

import com.shiftmanager.api.dto.NotificationDTO;
import com.shiftmanager.api.exception.ResourceNotFoundException;
import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Notification;
import com.shiftmanager.api.model.Shift;
import com.shiftmanager.api.model.VacationRequest;
import com.shiftmanager.api.repository.EmployeeRepository;
import com.shiftmanager.api.repository.NotificationRepository;
import com.shiftmanager.api.repository.ShiftRepository;
import com.shiftmanager.api.repository.VacationRequestRepository;
import com.shiftmanager.api.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
// Email functionality will be implemented later
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the NotificationService interface
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ShiftRepository shiftRepository;
    
    @Autowired
    private VacationRequestRepository vacationRequestRepository;
    
    // Email functionality will be implemented later
    // @Autowired(required = false)
    // private JavaMailSender emailSender;
    
    @Override
    public List<NotificationDTO> getNotificationsForEmployee(Long employeeId) {
        logger.debug("Getting notifications for employee ID: {}", employeeId);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        List<Notification> notifications = notificationRepository.findByEmployeeOrderByCreatedAtDesc(employee);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<NotificationDTO> getUnreadNotificationsForEmployee(Long employeeId) {
        logger.debug("Getting unread notifications for employee ID: {}", employeeId);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        List<Notification> notifications = notificationRepository.findByEmployeeAndReadFalseOrderByCreatedAtDesc(employee);
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public NotificationDTO markNotificationAsRead(Long notificationId) {
        logger.debug("Marking notification as read, ID: {}", notificationId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));
        
        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        
        Notification updatedNotification = notificationRepository.save(notification);
        return convertToDTO(updatedNotification);
    }
    
    @Override
    public NotificationDTO createNotification(Long employeeId, String message, String type, Long referenceId) {
        logger.debug("Creating notification for employee ID: {}, type: {}", employeeId, type);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        Notification notification = new Notification();
        notification.setEmployee(employee);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setReferenceId(referenceId);
        
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }
    
    @Override
    public void deleteNotification(Long notificationId) {
        logger.debug("Deleting notification with ID: {}", notificationId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + notificationId));
        
        notificationRepository.delete(notification);
    }
    
    @Override
    public void deleteAllNotificationsForEmployee(Long employeeId) {
        logger.debug("Deleting all notifications for employee ID: {}", employeeId);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        notificationRepository.deleteByEmployee(employee);
    }
    
    @Override
    public boolean sendEmailNotification(String email, String subject, String content) {
        logger.debug("Sending email notification to: {}, subject: {}", email, subject);
        
        // Email functionality will be implemented later
        logger.info("Would send email to {} with subject: {}", email, subject);
        logger.info("Email content: {}", content);
        return true;
    }
    
    @Override
    public NotificationDTO sendShiftAssignmentNotification(Long employeeId, Long shiftId) {
        logger.debug("Sending shift assignment notification to employee ID: {} for shift ID: {}", employeeId, shiftId);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ResourceNotFoundException("Shift not found with ID: " + shiftId));
        
        String message = "You have been assigned to a shift on " + shift.getShiftDate() +
                " at " + shift.getLocation().getName() + " (" + shift.getShiftType().getName() + " shift)";
        
        // Try to send email if employee has email address
        if (employee.getEmail() != null && !employee.getEmail().isEmpty()) {
            sendEmailNotification(
                    employee.getEmail(),
                    "New Shift Assignment",
                    message);
        }
        
        // Create in-app notification
        return createNotification(employeeId, message, "SHIFT_ASSIGNMENT", shiftId);
    }
    
    @Override
    public NotificationDTO sendVacationRequestStatusNotification(Long employeeId, Long vacationRequestId, boolean approved) {
        logger.debug("Sending vacation request {} notification to employee ID: {} for request ID: {}",
                approved ? "approval" : "rejection", employeeId, vacationRequestId);
        
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));
        
        VacationRequest request = vacationRequestRepository.findById(vacationRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation request not found with ID: " + vacationRequestId));
        
        String status = approved ? "approved" : "rejected";
        String message = "Your vacation request from " + request.getStartDate() + " to " + request.getEndDate() +
                " has been " + status;
        
        if (request.getReviewNotes() != null && !request.getReviewNotes().isEmpty()) {
            message += ". Note: " + request.getReviewNotes();
        }
        
        // Try to send email if employee has email address
        if (employee.getEmail() != null && !employee.getEmail().isEmpty()) {
            sendEmailNotification(
                    employee.getEmail(),
                    "Vacation Request " + (approved ? "Approved" : "Rejected"),
                    message);
        }
        
        // Create in-app notification
        return createNotification(employeeId, message, "VACATION_" + (approved ? "APPROVED" : "REJECTED"), vacationRequestId);
    }
    
    /**
     * Convert a Notification entity to a NotificationDTO
     * @param notification The notification entity
     * @return The notification DTO
     */
    private NotificationDTO convertToDTO(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .employeeId(notification.getEmployee().getId())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .referenceId(notification.getReferenceId())
                .build();
    }
}