package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Employee;
import com.shiftmanager.api.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Notification entity
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    /**
     * Find notifications by employee ordered by creation date descending
     * @param employee Employee
     * @return List of notifications
     */
    List<Notification> findByEmployeeOrderByCreatedAtDesc(Employee employee);
    
    /**
     * Find unread notifications by employee ordered by creation date descending
     * @param employee Employee
     * @return List of notifications
     */
    List<Notification> findByEmployeeAndReadFalseOrderByCreatedAtDesc(Employee employee);
    
    /**
     * Delete notifications by employee
     * @param employee Employee
     */
    void deleteByEmployee(Employee employee);
    
    /**
     * Find notifications by type
     * @param type Notification type
     * @return List of notifications
     */
    List<Notification> findByType(String type);
    
    /**
     * Find notifications by reference ID
     * @param referenceId Reference ID
     * @return List of notifications
     */
    List<Notification> findByReferenceId(Long referenceId);
    
    /**
     * Count unread notifications by employee
     * @param employee Employee
     * @return Count of unread notifications
     */
    int countByEmployeeAndReadFalse(Employee employee);
}