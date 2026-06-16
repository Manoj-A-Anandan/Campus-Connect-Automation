package com.campusconnect.repository;

import com.campusconnect.entity.PasswordReset;
import com.campusconnect.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByToken(String token);
    Optional<PasswordReset> findByUserAndUsedIsFalse(User user);
    void deleteByExpiryTimeBefore(LocalDateTime expiryTime);
}
