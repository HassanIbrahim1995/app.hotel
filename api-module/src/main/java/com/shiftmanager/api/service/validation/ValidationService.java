package com.shiftmanager.api.service.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for entity validation
 */
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;

    /**
     * Validate an entity
     * @param entity The entity to validate
     * @param <T> The entity type
     * @return List of validation error messages
     */
    public <T> List<String> validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        return violations.stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
    }

    /**
     * Check if an entity is valid
     * @param entity The entity to validate
     * @param <T> The entity type
     * @return true if valid
     */
    public <T> boolean isValid(T entity) {
        return validator.validate(entity).isEmpty();
    }

    /**
     * Validate and throw exception if invalid
     * @param entity The entity to validate
     * @param <T> The entity type
     * @throws ValidationException if validation fails
     */
    public <T> void validateAndThrow(T entity) throws ValidationException {
        List<String> errors = validate(entity);
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed", errors);
        }
    }
}
