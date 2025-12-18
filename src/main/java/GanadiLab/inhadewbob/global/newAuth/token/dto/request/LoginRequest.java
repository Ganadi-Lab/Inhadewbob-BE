package NotFound.next_campus.global.auth.token.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private Long studentId;
    private String password;
    private Boolean isWeb; // true -> web login (cookie), false -> app (json)

    public Boolean getIsWeb() {
        return isWeb == null ? false : isWeb;
    }
}
