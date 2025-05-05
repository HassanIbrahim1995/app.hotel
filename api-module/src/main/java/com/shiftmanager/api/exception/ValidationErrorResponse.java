package com.shiftmanager.api.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Validation error response DTO
 */
@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {

    /**
     * Validation errors
     */
    private List<String> errors;

    /**
     * Default constructor
     */
    public ValidationErrorResponse() {
        super();
    }

    /**
     * Constructor with all fields
     * @param status HTTP status code
     * @param message Error message
     * @param path Request path
     * @param errors List of validation errors
     */
    public ValidationErrorResponse(int status, String message, String path, List<String> errors) {
        super(status, message, path);
        this.errors = errors;
    }
}
