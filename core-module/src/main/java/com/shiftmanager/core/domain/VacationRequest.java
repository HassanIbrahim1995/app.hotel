package com.shiftmanager.core.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Entity representing a vacation request
 */
@Entity
@Table(name = "vacation_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VacationStatus status;

    private String reason;

    private String managerComments;

    private LocalDateTime reviewedAt;

    @NotNull
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = VacationStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Calculates the number of vacation days requested
     * @return Number of days as a long
     */
    @Transient
    public long getDaysCount() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1; // Including both start and end date
    }

    /**
     * Approves this vacation request
     * @param manager The manager approving the request
     * @param comments Optional comments from the manager
     */
    public void approve(Manager manager, String comments) {
        this.status = VacationStatus.APPROVED;
        this.manager = manager;
        this.managerComments = comments;
        this.reviewedAt = LocalDateTime.now();
    }

    /**
     * Rejects this vacation request
     * @param manager The manager rejecting the request
     * @param comments Required comments explaining the rejection reason
     */
    public void reject(Manager manager, String comments) {
        this.status = VacationStatus.REJECTED;
        this.manager = manager;
        this.managerComments = comments;
        this.reviewedAt = LocalDateTime.now();
    }
}
