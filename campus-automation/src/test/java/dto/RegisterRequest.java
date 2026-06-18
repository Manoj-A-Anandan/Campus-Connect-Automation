package dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String email;
    private String fullName;
    private String password;
    private String confirmPassword;
    private String role; // STUDENT, FACULTY, ADMIN
}