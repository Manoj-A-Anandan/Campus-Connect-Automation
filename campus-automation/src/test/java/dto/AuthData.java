package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthData {
    private String token;
    private String refreshToken;
    private String email;
    private String fullName;
    private String role;
    private Long userId;
    private String message;
}