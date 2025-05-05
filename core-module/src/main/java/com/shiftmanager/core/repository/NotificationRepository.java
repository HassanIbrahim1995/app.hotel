package com.shiftmanager.core.repository;

import com.shiftmanager.core.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Notification entity operations
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Find notifications by employee
     * @param employeeId The employee ID
     * @return List of notifications for the specified employee
     */
    List<Notification> findByEmployeeId(Long employeeId);
    
    /**
     * Find unread notifications by employee
     * @param employeeId The employee ID
     * @return List of unread notifications for the specified employee
     */
    List<Notification> findByEmployeeIdAndReadFalse(Long employeeId);
    
    /**
     * Find read notifications by employee
     * @param employeeId The employee ID
     * @return List of read notifications for the specified employee
     */
    List<Notification> findByEmployeeIdAndReadTrue(Long employeeId);
    
    /**
     * Find notifications where email has not been sent
     * @return List of notifications where email has not been sent
     */
    List<Notification> findByEmailSentFalse();
    
    /**
     * Find notifications created after a specific date
     * @param dateTime The date/time to compare against
     * @return List of notifications created after the specified date/time
     */
    List<Notification> findByCreatedAtAfter(LocalDateTime dateTime);
    
    /**
     * Find notifications by employee and creation date range
     * @param employeeId The employee ID
     * @param startDateTime The start of the date range
     * @param endDateTime The end of the date range
     * @return List of notifications for the specified employee within the date range
     */
    List<Notification> findByEmployeeIdAndCreatedAtBetween(
            Long employeeId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    /**
     * Count unread notifications for an employee
     * @param employeeId The employee ID
     * @return The count of unread notifications
     */
    Long countByEmployeeIdAndReadFalse(Long employeeId);
}
