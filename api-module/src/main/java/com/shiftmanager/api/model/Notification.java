package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Notification entity for sending notifications to employees
 */
@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotBlank(message = "Message is required")
    @Size(max = 500, message = "Message cannot exceed 500 characters")
    @Column(name = "message", nullable = false)
    private String message;

    @NotBlank(message = "Type is required")
    @Size(max = 50, message = "Type cannot exceed 50 characters")
    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "is_read", nullable = false)
    private boolean read = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    @Column(name = "reference_id")
    private Long referenceId;

    /**
     * Constructor with required fields
     * @param employee The employee to notify
     * @param message Notification message
     * @param type Notification type (e.g., SHIFT_ASSIGNMENT, VACATION_APPROVED)
     */
    public Notification(Employee employee, String message, String type) {
        this.employee = employee;
        this.message = message;
        this.type = type;
        this.read = false;
    }
    
    /**
     * Constructor with reference ID
     * @param employee The employee to notify
     * @param message Notification message
     * @param type Notification type
     * @param referenceId Reference ID to related entity
     */
    public Notification(Employee employee, String message, String type, Long referenceId) {
        this.employee = employee;
        this.message = message;
        this.type = type;
        this.read = false;
        this.referenceId = referenceId;
    }

    /**
     * Mark notification as read
     */
    public void markAsRead() {
        this.read = true;
        this.readAt = LocalDateTime.now();
    }
}
