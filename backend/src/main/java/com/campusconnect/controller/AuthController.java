package com.campusconnect.controller;

import com.campusconnect.dto.*;
import com.campusconnect.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and Authorization APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        log.info("Register request for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(
                ApiResponse.success("User registered successfully", response),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and get JWT token")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return new ResponseEntity<>(
                ApiResponse.success("Login successful", response),
                HttpStatus.OK
        );
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot password", description = "Request password reset link")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        log.info("Forgot password request for email: {}", request.getEmail());
        AuthResponse response = authService.forgotPassword(request);
        return new ResponseEntity<>(
                ApiResponse.success(response.getMessage()),
                HttpStatus.OK
        );
    }

    @GetMapping("/test/reset-token")
    @Operation(summary = "Get reset token for testing", description = "Retrieve the latest active reset token for a user")
    public ResponseEntity<ApiResponse> getResetTokenForTesting(@RequestParam String email) {
        log.info("Test request to get reset token for email: {}", email);
        String token = authService.getLatestResetTokenForEmail(email);
        return new ResponseEntity<>(
                ApiResponse.success("Token retrieved successfully", token),
                HttpStatus.OK
        );
    }

    @PostMapping("/validate-token/{token}")
    @Operation(summary = "Validate reset token", description = "Check if password reset token is valid")
    public ResponseEntity<ApiResponse> validateToken(@PathVariable String token) {
        log.info("Validating password reset token");
        AuthResponse response = authService.validateResetToken(token);
        return new ResponseEntity<>(
                ApiResponse.success(response.getMessage()),
                HttpStatus.OK
        );
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset password", description = "Reset user password with valid token")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        log.info("Reset password request");
        AuthResponse response = authService.resetPassword(request);
        return new ResponseEntity<>(
                ApiResponse.success(response.getMessage()),
                HttpStatus.OK
        );
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check API health status")
    public ResponseEntity<ApiResponse> health() {
        return new ResponseEntity<>(
                ApiResponse.success("API is running"),
                HttpStatus.OK
        );
    }
}
