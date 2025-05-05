package com.shiftmanager.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simplified application class for the Shift Manager API
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@RestController
public class SimplifiedApplication {

    @GetMapping("/")
    public String home() {
        return "Shift Manager API is running!";
    }
    
    @GetMapping("/health")
    public String health() {
        return "UP";
    }

    public static void main(String[] args) {
        SpringApplication.run(SimplifiedApplication.class, args);
    }
}
