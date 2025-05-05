package com.shiftmanager.api.service.validation;

import java.util.List;

/**
 * Exception thrown when validation fails
 */
public class ValidationException extends RuntimeException {

    private final List<String> errors;

    /**
     * Constructor with message and errors
     * @param message The error message
     * @param errors The validation errors
     */
    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    /**
     * Get the validation errors
     * @return List of error messages
     */
    public List<String> getErrors() {
        return errors;
    }
}
