package com.shiftmanager.core.repository;

import com.shiftmanager.core.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Base repository for Person entity operations
 */
@NoRepositoryBean
public interface PersonRepository<T extends Person> extends JpaRepository<T, Long> {
    
    /**
     * Find a person by their email address
     * @param email The email to search for
     * @return Optional containing the person if found
     */
    Optional<T> findByEmail(String email);
    
    /**
     * Find a person by their phone number
     * @param phone The phone number to search for
     * @return Optional containing the person if found
     */
    Optional<T> findByPhone(String phone);
    
    /**
     * Find people by their location
     * @param locationId The location ID
     * @return List of people at the specified location
     */
    List<T> findByLocationId(Long locationId);
    
    /**
     * Find people by first name
     * @param firstName The first name to search for
     * @return List of people with the specified first name
     */
    List<T> findByNameFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * Find people by last name
     * @param lastName The last name to search for
     * @return List of people with the specified last name
     */
    List<T> findByNameLastNameContainingIgnoreCase(String lastName);
}
