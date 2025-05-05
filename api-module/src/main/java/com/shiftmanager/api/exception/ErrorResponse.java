package com.shiftmanager.api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Standard error response DTO
 */
@Getter
@Setter
public class ErrorResponse {

    /**
     * HTTP status code
     */
    private int status;

    /**
     * Error message
     */
    private String message;

    /**
     * Request path
     */
    private String path;

    /**
     * Timestamp
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    /**
     * Default constructor
     */
    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor with all fields
     * @param status HTTP status code
     * @param message Error message
     * @param path Request path
     */
    public ErrorResponse(int status, String message, String path) {
        this();
        this.status = status;
        this.message = message;
        this.path = path;
    }
}
