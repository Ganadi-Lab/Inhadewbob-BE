package GanadiLab.inhadewbob.global.newAuth.token.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;  // API 요청 시 Authorization 헤더에 넣음
    private String refreshToken; // 앱 전용 (웹은 HttpOnly 쿠키)
}
