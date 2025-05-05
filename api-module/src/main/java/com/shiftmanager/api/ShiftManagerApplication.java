package com.shiftmanager.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Shift Manager API
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.shiftmanager")
@EntityScan(basePackages = "com.shiftmanager.core.domain")
@EnableJpaRepositories(basePackages = "com.shiftmanager.core.repository")
@EnableScheduling
public class ShiftManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiftManagerApplication.class, args);
    }
}
