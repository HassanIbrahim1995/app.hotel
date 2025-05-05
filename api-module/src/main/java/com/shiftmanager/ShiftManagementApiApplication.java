package com.shiftmanager.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for the Shift Management API
 */
@SpringBootApplication
@EntityScan(basePackages = "com.shiftmanager.api.model")
@EnableJpaRepositories(basePackages = "com.shiftmanager.api.repository")
@EnableJpaAuditing
public class ShiftManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiftManagementApiApplication.class, args);
    }
}
