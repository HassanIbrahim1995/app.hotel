package com.shiftmanager.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "manager")
@DiscriminatorValue("MANAGER")
@Getter
@Setter
@NoArgsConstructor
public class Manager extends Person {

    @Column(name = "manager_id", nullable = false, unique = true)
    private String managerId;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "department")
    private String department;

    @Column(name = "position")
    private String position;

    @Column(name = "authorization_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorizationLevel authorizationLevel;

    public Manager(String firstName, String lastName, String email, String managerId, LocalDate hireDate, AuthorizationLevel authorizationLevel) {
        super(firstName, lastName, email);
        this.managerId = managerId;
        this.hireDate = hireDate;
        this.authorizationLevel = authorizationLevel;
    }

    public enum AuthorizationLevel {
        JUNIOR, SENIOR, DEPARTMENT_HEAD, DIRECTOR
    }
}
