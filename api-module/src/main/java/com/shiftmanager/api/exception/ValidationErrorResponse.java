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
     * Create an exception with this validation error
     * @param message Error message
     * @return ValidationException
     */
    public static ValidationException exception(String message) {
        return new ValidationException(message);
    }
    
    /**
     * Create an exception with this validation error
     * @param message Error message
     * @param errors List of validation errors
     * @return ValidationException
     */
    public static ValidationException exception(String message, List<String> errors) {
        return new ValidationException(message, errors);
    }

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
    
    /**
     * Constructor with message only
     * @param message Error message
     */
    public ValidationErrorResponse(String message) {
        super(400, message, null);
    }
}
