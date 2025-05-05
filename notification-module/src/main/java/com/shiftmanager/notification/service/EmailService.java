package com.shiftmanager.notification.service;

/**
 * Service interface for Email operations
 */
public interface EmailService {

    /**
     * Send a notification email
     * @param to The recipient email address
     * @param subject The email subject
     * @param content The email content
     */
    void sendNotificationEmail(String to, String subject, String content);

    /**
     * Send a shift assignment email
     * @param to The recipient email address
     * @param employeeName The employee's name
     * @param shiftDetails The shift details
     */
    void sendShiftAssignmentEmail(String to, String employeeName, String shiftDetails);

    /**
     * Send a vacation status update email
     * @param to The recipient email address
     * @param employeeName The employee's name
     * @param status The vacation request status
     * @param details The vacation request details
     */
    void sendVacationStatusEmail(String to, String employeeName, String status, String details);

    /**
     * Send a schedule update email
     * @param to The recipient email address
     * @param employeeName The employee's name
     * @param scheduleDetails The schedule details
     */
    void sendScheduleUpdateEmail(String to, String employeeName, String scheduleDetails);
}
