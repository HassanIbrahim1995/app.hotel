package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Person entity
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    /**
     * Find a person by email
     * @param email The email to search for
     * @return Optional person
     */
    Optional<Person> findByEmail(String email);
    
    /**
     * Check if a person exists with the given email
     * @param email The email to check
     * @return true if exists
     */
    boolean existsByEmail(String email);
}
