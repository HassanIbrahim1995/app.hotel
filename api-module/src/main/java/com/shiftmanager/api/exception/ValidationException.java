package com.shiftmanager.api.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Exception for validation errors
 */
public class ValidationException extends RuntimeException {
    
    private List<String> errors;
    
    /**
     * Constructor with error message
     * @param message Error message
     */
    public ValidationException(String message) {
        super(message);
        this.errors = new ArrayList<>();
    }
    
    /**
     * Constructor with error message and list of validation errors
     * @param message Error message
     * @param errors List of validation errors
     */
    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }
    
    /**
     * Get the list of validation errors
     * @return List of validation errors
     */
    public List<String> getErrors() {
        return errors;
    }
}