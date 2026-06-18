package com.campusconnect.service;

import com.campusconnect.dto.*;
import com.campusconnect.entity.User;
import com.campusconnect.entity.PasswordReset;
import com.campusconnect.exception.ResourceNotFoundException;
import com.campusconnect.exception.ValidationException;
import com.campusconnect.repository.UserRepository;
import com.campusconnect.repository.PasswordResetRepository;
import com.campusconnect.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        // Validate full name
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new ValidationException("name should not be empty");
        }

        // Validate email format
        if (!isValidEmail(request.getEmail())) {
            throw new ValidationException("Invalid email format");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already registered");
        }

        // Validate password
        validatePassword(request.getPassword(), request.getConfirmPassword());

        // Create new user
        User user = User.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.valueOf(request.getRole().toUpperCase()))
                .active(true)
                .build();

        user = userRepository.save(user);
        log.info("User registered successfully with id: {}", user.getId());

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .userId(user.getId())
                .message("Registration successful")
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        // Find user by email
        User user = userRepository.findByEmailAndActive(request.getEmail(), true)
                .orElseThrow(() -> new ValidationException("Invalid email or password"));

        // Validate password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ValidationException("Invalid email or password");
        }

        log.info("User logged in successfully: {}", user.getEmail());

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name(), user.getId());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole().name())
                .userId(user.getId())
                .message("Login successful")
                .build();
    }

    @Transactional
    public AuthResponse forgotPassword(ForgotPasswordRequest request) {
        log.info("Forgot password request for email: {}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + request.getEmail()));

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        PasswordReset passwordReset = PasswordReset.builder()
                .user(user)
                .token(resetToken)
                .used(false)
                .build();

        passwordResetRepository.save(passwordReset);

        // Send email (placeholder)
        String resetLink = "http://localhost:3000/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), user.getFullName(), resetLink);

        log.info("Password reset email sent to: {}", user.getEmail());

        return AuthResponse.builder()
                .message("Password reset link sent to your email")
                .build();
    }

    @Transactional
    public AuthResponse validateResetToken(String token) {
        log.info("Validating password reset token");

        PasswordReset passwordReset = passwordResetRepository.findByToken(token)
                .orElseThrow(() -> new ValidationException("Invalid or expired reset token"));

        if (passwordReset.getUsed() || passwordReset.isExpired()) {
            throw new ValidationException("Reset token has expired or already used");
        }

        return AuthResponse.builder()
                .message("Token is valid")
                .build();
    }

    @Transactional
    public AuthResponse resetPassword(ResetPasswordRequest request) {
        log.info("Resetting password");

        PasswordReset passwordReset = passwordResetRepository.findByToken(request.getToken())
                .orElseThrow(() -> new ValidationException("Invalid or expired reset token"));

        if (passwordReset.getUsed() || passwordReset.isExpired()) {
            throw new ValidationException("Reset token has expired or already used");
        }

        validatePassword(request.getNewPassword(), request.getConfirmPassword());

        User user = passwordReset.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        passwordReset.setUsed(true);
        passwordResetRepository.save(passwordReset);

        log.info("Password reset successfully for email: {}", user.getEmail());

        return AuthResponse.builder()
                .message("Password reset successfully")
                .build();
    }

    @Transactional(readOnly = true)
    public String getLatestResetTokenForEmail(String email) {
        log.info("Fetching reset token for email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        PasswordReset passwordReset = passwordResetRepository.findFirstByUserAndUsedIsFalseOrderByCreatedAtDesc(user)
                .orElseThrow(() -> new ResourceNotFoundException("No active reset token found for user: " + email));

        return passwordReset.getToken();
    }

    private boolean isValidEmail(String email) {
        return pattern.matcher(email).matches();
    }

    private void validatePassword(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ValidationException("Passwords do not match");
        }

        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("Password must contain at least one uppercase letter");
        }

        if (!password.matches(".*[a-z].*")) {
            throw new ValidationException("Password must contain at least one lowercase letter");
        }

        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("Password must contain at least one digit");
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new ValidationException("Password must contain at least one special character");
        }
    }
}
