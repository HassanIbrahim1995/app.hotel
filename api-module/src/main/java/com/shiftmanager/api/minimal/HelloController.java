package com.shiftmanager.api.minimal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from Shift Manager API!";
    }
    
    @GetMapping("/api/status")
    public String status() {
        return "UP";
    }
}
