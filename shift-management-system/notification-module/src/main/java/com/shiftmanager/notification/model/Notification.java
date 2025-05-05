package com.shiftmanager.notification.model;

import com.shiftmanager.common.model.BaseEntity;
import com.shiftmanager.employeemanagement.model.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity representing a notification sent to an employee
 */
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Employee recipient;

    @NotBlank
    @Size(max = 100)
    @Column(name = "subject", nullable = false)
    private String subject;

    @NotBlank
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_method", nullable = false)
    private NotificationMethod notificationMethod;

    @Column(name = "read")
    private boolean read = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "sent")
    private boolean sent = false;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "delivery_failed")
    private boolean deliveryFailed = false;

    @Size(max = 255)
    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Column(name = "next_retry_at")
    private LocalDateTime nextRetryAt;

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @Size(max = 50)
    @Column(name = "related_entity_type")
    private String relatedEntityType;

    /**
     * Mark notification as read
     */
    public void markAsRead() {
        this.read = true;
        this.readAt = LocalDateTime.now();
    }

    /**
     * Mark notification as sent
     */
    public void markAsSent() {
        this.sent = true;
        this.sentAt = LocalDateTime.now();
    }

    /**
     * Mark notification as failed and set reason
     * @param reason failure reason
     */
    public void markAsFailed(String reason) {
        this.deliveryFailed = true;
        this.failureReason = reason;
        this.retryCount++;
    }

    /**
     * Schedule notification for retry
     * @param retryTime when to retry
     */
    public void scheduleRetry(LocalDateTime retryTime) {
        this.nextRetryAt = retryTime;
    }
}
