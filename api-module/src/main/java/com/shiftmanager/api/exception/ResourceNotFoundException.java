package com.shiftmanager.api.exception;

/**
 * Exception thrown when a resource is not found
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor with message
     * @param message The error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with message and cause
     * @param message The error message
     * @param cause The cause
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
