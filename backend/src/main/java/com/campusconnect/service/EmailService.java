package com.campusconnect.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String email, String fullName, String resetLink) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Campus Connect - Password Reset Request");
            message.setText("Hi " + fullName + ",\n\n" +
                    "You requested to reset your password. Click the link below to proceed:\n\n" +
                    resetLink + "\n\n" +
                    "This link will expire in 30 minutes.\n\n" +
                    "If you did not request this, please ignore this email.\n\n" +
                    "Best regards,\n" +
                    "Campus Connect Team");
            message.setFrom("noreply@campusconnect.com");

            // Uncomment when email is configured
            // mailSender.send(message);
            log.info("Password reset email sent to: {}. Reset Link: {}", email, resetLink);
        } catch (Exception ex) {
            log.error("Failed to send password reset email to: {}", email, ex);
        }
    }

    public void sendRegistrationEmail(String email, String fullName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Campus Connect - Registration Successful");
            message.setText("Hi " + fullName + ",\n\n" +
                    "Welcome to Campus Connect! Your account has been created successfully.\n\n" +
                    "You can now log in and start using the platform.\n\n" +
                    "Best regards,\n" +
                    "Campus Connect Team");
            message.setFrom("noreply@campusconnect.com");

            // Uncomment when email is configured
            // mailSender.send(message);
            log.info("Registration email sent to: {}", email);
        } catch (Exception ex) {
            log.error("Failed to send registration email to: {}", email, ex);
        }
    }
}
