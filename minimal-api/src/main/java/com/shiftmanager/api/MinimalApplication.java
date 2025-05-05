package com.shiftmanager.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MinimalApplication {

    @GetMapping("/")
    public String home() {
        return "Shift Manager Minimal API is running!";
    }

    public static void main(String[] args) {
        SpringApplication.run(MinimalApplication.class, args);
    }
}
