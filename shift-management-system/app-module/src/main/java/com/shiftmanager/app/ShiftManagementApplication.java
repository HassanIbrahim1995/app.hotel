package com.shiftmanager.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for the Shift Management System
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.shiftmanager")
@EntityScan(basePackages = "com.shiftmanager")
@EnableJpaRepositories(basePackages = "com.shiftmanager")
@EnableJpaAuditing
public class ShiftManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiftManagementApplication.class, args);
    }
}
