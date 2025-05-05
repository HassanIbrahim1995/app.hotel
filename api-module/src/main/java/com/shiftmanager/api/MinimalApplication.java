package com.shiftmanager.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Minimal application to verify Spring Boot runs correctly
 */
@SpringBootApplication(
    exclude = {
        DataSourceAutoConfiguration.class,
        SecurityAutoConfiguration.class
    }
)
@RestController
public class MinimalApplication {

    @GetMapping("/")
    public String home() {
        return "Shift Manager API (Minimal Version) is running!";
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

    public static void main(String[] args) {
        SpringApplication.run(MinimalApplication.class, args);
    }
}
