package com.shiftmanager.api.repository;

import com.shiftmanager.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by username
     * @param username Username
     * @return Optional user
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email
     * @param email Email
     * @return Optional user
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by username or email (for login)
     * @param username Username
     * @param email Email
     * @return Optional user
     */
    Optional<User> findByUsernameOrEmail(String username, String email);
    
    /**
     * Check if a username exists
     * @param username Username
     * @return True if username exists
     */
    Boolean existsByUsername(String username);
    
    /**
     * Check if an email exists
     * @param email Email
     * @return True if email exists
     */
    Boolean existsByEmail(String email);
    
    /**
     * Find a user by employee ID
     * @param employeeId Employee ID
     * @return Optional user
     */
    Optional<User> findByEmployeeId(Long employeeId);
}