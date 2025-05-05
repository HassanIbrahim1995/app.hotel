package com.shiftmanager.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple controller to check API status
 */
@RestController
@RequestMapping("/status")
public class StatusController {

    /**
     * Get API status
     * @return Status information as a map
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", LocalDateTime.now().toString());
        status.put("application", "Shift Management System");
        status.put("version", "0.0.1-SNAPSHOT");

        return ResponseEntity.ok(status);
    }
}
