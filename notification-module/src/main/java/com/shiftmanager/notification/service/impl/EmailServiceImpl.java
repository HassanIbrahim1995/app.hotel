package com.shiftmanager.notification.service.impl;

import com.shiftmanager.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * Implementation of EmailService
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String senderEmail = "noreply@shiftmanager.com";

    @Override
    public void sendNotificationEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        
        mailSender.send(message);
    }

    @Override
    public void sendShiftAssignmentEmail(String to, String employeeName, String shiftDetails) {
        String subject = "Shift Assignment Notification";
        String content = buildShiftAssignmentEmailContent(employeeName, shiftDetails);
        
        sendHtmlEmail(to, subject, content);
    }

    @Override
    public void sendVacationStatusEmail(String to, String employeeName, String status, String details) {
        String subject = "Vacation Request Status Update: " + status;
        String content = buildVacationStatusEmailContent(employeeName, status, details);
        
        sendHtmlEmail(to, subject, content);
    }

    @Override
    public void sendScheduleUpdateEmail(String to, String employeeName, String scheduleDetails) {
        String subject = "Schedule Update Notification";
        String content = buildScheduleUpdateEmailContent(employeeName, scheduleDetails);
        
        sendHtmlEmail(to, subject, content);
    }

    /**
     * Sends an HTML email
     * @param to The recipient email address
     * @param subject The email subject
     * @param htmlContent The HTML content
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    /**
     * Builds the email content for shift assignments
     * @param employeeName The employee's name
     * @param shiftDetails The shift details
     * @return The HTML content
     */
    private String buildShiftAssignmentEmailContent(String employeeName, String shiftDetails) {
        return "<html><body>" +
                "<h2>Shift Assignment</h2>" +
                "<p>Hello " + employeeName + ",</p>" +
                "<p>You have been assigned to a new shift:</p>" +
                "<div style='padding: 15px; background-color: #f5f5f5; border-left: 4px solid #4CAF50;'>" +
                "<p>" + shiftDetails + "</p>" +
                "</div>" +
                "<p>Please log in to your account to view the complete details.</p>" +
                "<p>Thank you,<br/>Shift Manager Team</p>" +
                "</body></html>";
    }

    /**
     * Builds the email content for vacation status updates
     * @param employeeName The employee's name
     * @param status The vacation request status
     * @param details The vacation request details
     * @return The HTML content
     */
    private String buildVacationStatusEmailContent(String employeeName, String status, String details) {
        String statusColor;
        switch (status) {
            case "APPROVED":
                statusColor = "#4CAF50"; // Green
                break;
            case "REJECTED":
                statusColor = "#F44336"; // Red
                break;
            default:
                statusColor = "#FFC107"; // Yellow
                break;
        }
        
        return "<html><body>" +
                "<h2>Vacation Request Status Update</h2>" +
                "<p>Hello " + employeeName + ",</p>" +
                "<p>Your vacation request status has been updated to: " +
                "<span style='font-weight: bold; color: " + statusColor + ";'>" + status + "</span></p>" +
                "<div style='padding: 15px; background-color: #f5f5f5; border-left: 4px solid " + statusColor + ";'>" +
                "<p>" + details + "</p>" +
                "</div>" +
                "<p>Please log in to your account to view the complete details.</p>" +
                "<p>Thank you,<br/>Shift Manager Team</p>" +
                "</body></html>";
    }

    /**
     * Builds the email content for schedule updates
     * @param employeeName The employee's name
     * @param scheduleDetails The schedule details
     * @return The HTML content
     */
    private String buildScheduleUpdateEmailContent(String employeeName, String scheduleDetails) {
        return "<html><body>" +
                "<h2>Schedule Update</h2>" +
                "<p>Hello " + employeeName + ",</p>" +
                "<p>Your schedule has been updated:</p>" +
                "<div style='padding: 15px; background-color: #f5f5f5; border-left: 4px solid #2196F3;'>" +
                "<p>" + scheduleDetails + "</p>" +
                "</div>" +
                "<p>Please log in to your account to view the complete schedule.</p>" +
                "<p>Thank you,<br/>Shift Manager Team</p>" +
                "</body></html>";
    }
}
