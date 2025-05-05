package com.shiftmanager.api.exception;

import com.shiftmanager.api.service.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for REST controllers using RFC 7807 Problem Details
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String ERROR_URI_BASE = "https://api.shiftmanager.com/errors";

    /**
     * Handle ResourceNotFoundException
     * @param ex The exception
     * @param request The web request
     * @return Response with RFC 7807 problem details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        log.error("Resource not found: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(URI.create(ERROR_URI_BASE + "/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("request", request.getDescription(false));
        
        return problemDetail;
    }

    /**
     * Handle EntityNotFoundException
     * @param ex The exception
     * @param request The web request
     * @return Response with RFC 7807 problem details
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(
            EntityNotFoundException ex, WebRequest request) {
        log.error("Entity not found: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setTitle("Entity Not Found");
        problemDetail.setType(URI.create(ERROR_URI_BASE + "/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("request", request.getDescription(false));
        
        return problemDetail;
    }

    /**
     * Handle ValidationException
     * @param ex The exception
     * @param request The web request
     * @return Response with RFC 7807 problem details
     */
    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidationException(
            ValidationException ex, WebRequest request) {
        log.error("Validation error: {}", ex.getMessage());
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create(ERROR_URI_BASE + "/validation"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("request", request.getDescription(false));
        problemDetail.setProperty("errors", ex.getErrors());
        
        return problemDetail;
    }

    /**
     * Handle MethodArgumentNotValidException
     * @param ex The exception
     * @param request The web request
     * @return Response with RFC 7807 problem details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Validation error: {}", ex.getMessage());
        
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create(ERROR_URI_BASE + "/validation"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("request", request.getDescription(false));
        problemDetail.setProperty("fieldErrors", fieldErrors);
        
        return problemDetail;
    }

    /**
     * Handle all other exceptions
     * @param ex The exception
     * @param request The web request
     * @return Response with RFC 7807 problem details
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create(ERROR_URI_BASE + "/server-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        problemDetail.setProperty("request", request.getDescription(false));
        // Don't include stack trace in production for security reasons
        
        return problemDetail;
    }
}
