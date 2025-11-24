package GanadiLab.inhadewbob.global.oauth;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.global.config.auth.JwtTokenProvider;
import GanadiLab.inhadewbob.global.oauth.GoogleOAuthService;
import GanadiLab.inhadewbob.global.oauth.dto.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {

    private final GoogleOAuthService googleOAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    // 1) 프론트 or Postman이 구글 로그인 URL 요청
    @GetMapping("/login/google")
    public ResponseEntity<?> googleLoginUrl() {

        String url = "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + googleOAuthService.getClientId()
                + "&redirect_uri=" + googleOAuthService.getRedirectUri()
                + "&response_type=code"
                + "&scope=openid%20email%20profile";

        return ResponseEntity.ok(url);
    }

    // 2) 구글 로그인 후 redirect_uri로 code 전달됨
    @GetMapping("/login/google/callback")
    public ResponseEntity<?> googleCallback(@RequestParam("code") String code) {

        // 1) code → access_token
        String accessToken = googleOAuthService.getAccessToken(code);

        // 2) 사용자 정보 조회
        GoogleUserInfo userInfo = googleOAuthService.getUserInfo(accessToken);

        // 3) DB 조회/생성
        Member member = googleOAuthService.findOrCreate(userInfo);

        // 4) JWT 발급
        String jwt = jwtTokenProvider.createAccessToken(member.getId());

        return ResponseEntity.ok(jwt);
    }
}