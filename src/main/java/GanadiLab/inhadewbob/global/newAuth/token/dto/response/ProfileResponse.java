package NotFound.next_campus.global.auth.token.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    private Long studentId;
    private String name;
    private String email;
    private String department;
    private String role;
}
