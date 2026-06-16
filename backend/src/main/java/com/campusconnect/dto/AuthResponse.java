package com.campusconnect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String email;
    private String fullName;
    private String role;
    private Long userId;
    private String message;
}
