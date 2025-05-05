package com.shiftmanager.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User entity for authentication and authorization
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(name = "password", nullable = false)
    private String password;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    
    @Column(name = "active", nullable = false)
    private boolean active = true;
    
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;
    
    /**
     * Basic constructor
     * @param username Username
     * @param email Email
     * @param password Password (encrypted)
     */
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.active = true;
    }
    
    /**
     * Full constructor with employee
     * @param username Username
     * @param email Email
     * @param password Password (encrypted)
     * @param employee Associated employee
     */
    public User(String username, String email, String password, Employee employee) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.employee = employee;
        this.active = true;
    }
    
    /**
     * Add a role to the user
     * @param role Role to add
     */
    public void addRole(Role role) {
        roles.add(role);
    }
    
    /**
     * Remove a role from the user
     * @param role Role to remove
     */
    public void removeRole(Role role) {
        roles.remove(role);
    }
    
    /**
     * Record login
     */
    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
    
    /**
     * Check if user has a specific role
     * @param roleName Role name
     * @return True if user has the role
     */
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }
    
    /**
     * Check if user is an administrator
     * @return True if user is an administrator
     */
    @Transient
    public boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
    
    /**
     * Check if user is a manager
     * @return True if user is a manager
     */
    @Transient
    public boolean isManager() {
        return hasRole("ROLE_MANAGER");
    }
}