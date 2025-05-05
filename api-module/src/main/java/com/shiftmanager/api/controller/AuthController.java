package com.shiftmanager.api.controller;

import com.shiftmanager.api.dto.LoginRequestDTO;
import com.shiftmanager.api.dto.LoginResponseDTO;
import com.shiftmanager.api.model.User;
import com.shiftmanager.api.repository.UserRepository;
import com.shiftmanager.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for authentication operations
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    /**
     * Authenticate user and generate JWT token
     * @param loginRequest Login credentials
     * @return JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = tokenProvider.generateToken(authentication);
        
        // Get user details
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create response
        LoginResponseDTO response = new LoginResponseDTO();
        response.setAccessToken(jwt);
        response.setTokenType("Bearer");
        response.setUsername(user.getUsername());
        response.setEmployeeId(user.getEmployee().getId());
        response.setRoles(authentication.getAuthorities().stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    /**
     * Check if token is valid
     * @return User info
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken() {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Get user details
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Create response
        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("employeeId", user.getEmployee().getId());
        response.put("roles", authentication.getAuthorities().stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.toList()));

        return ResponseEntity.ok(response);
    }
}