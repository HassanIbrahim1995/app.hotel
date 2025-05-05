package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * VacationRequest entity for handling employee vacation/time-off requests
 */
@Entity
@Table(name = "vacation_request")
@Getter
@Setter
@NoArgsConstructor
public class VacationRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotBlank(message = "Status is required")
    @Size(max = 20, message = "Status cannot exceed 20 characters")
    @Column(name = "status", nullable = false)
    private String status;

    @Size(max = 255, message = "Request notes cannot exceed 255 characters")
    @Column(name = "request_notes")
    private String requestNotes;

    @Size(max = 255, message = "Review notes cannot exceed 255 characters")
    @Column(name = "review_notes")
    private String reviewNotes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewer_id")
    private Employee reviewer;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    /**
     * Constructor with required fields
     * @param employee Employee requesting vacation
     * @param startDate Start date of vacation
     * @param endDate End date of vacation
     * @param requestNotes Optional notes about the request
     */
    public VacationRequest(Employee employee, LocalDate startDate, LocalDate endDate, String requestNotes) {
        this.employee = employee;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestNotes = requestNotes;
        this.status = "PENDING"; // Default status
    }

    /**
     * Calculate the number of days requested
     * @return Number of days
     */
    @Transient
    public long getDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to include both start and end days
    }

    /**
     * Check if request is pending
     * @return true if pending
     */
    @Transient
    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }

    /**
     * Check if request is approved
     * @return true if approved
     */
    @Transient
    public boolean isApproved() {
        return "APPROVED".equalsIgnoreCase(status);
    }

    /**
     * Check if request is rejected
     * @return true if rejected
     */
    @Transient
    public boolean isRejected() {
        return "REJECTED".equalsIgnoreCase(status);
    }

    /**
     * Approve the vacation request
     * @param reviewer The employee approving the request
     * @param notes Optional approval notes
     */
    public void approve(Employee reviewer, String notes) {
        this.status = "APPROVED";
        this.reviewer = reviewer;
        this.reviewNotes = notes;
        this.reviewedAt = LocalDateTime.now();
    }

    /**
     * Reject the vacation request
     * @param reviewer The employee rejecting the request
     * @param notes Optional rejection notes
     */
    public void reject(Employee reviewer, String notes) {
        this.status = "REJECTED";
        this.reviewer = reviewer;
        this.reviewNotes = notes;
        this.reviewedAt = LocalDateTime.now();
    }
}
