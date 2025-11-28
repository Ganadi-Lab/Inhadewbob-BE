package GanadiLab.inhadewbob.global.oauth;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import GanadiLab.inhadewbob.global.config.auth.JwtTokenProvider;
import GanadiLab.inhadewbob.global.config.auth.PrincipalDetails;
import GanadiLab.inhadewbob.global.oauth.GoogleOAuthService;
import GanadiLab.inhadewbob.global.oauth.dto.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class OAuthController {

    private final GoogleOAuthService googleOAuthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

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

        String googleAccessToken = googleOAuthService.getAccessToken(code);
        GoogleUserInfo userInfo = googleOAuthService.getUserInfo(googleAccessToken);

        Member member = googleOAuthService.findOrCreate(userInfo);

        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        member.setRefreshToken(refreshToken);
        memberRepository.save(member);

        return ResponseEntity.ok(
                Map.of(
                        "access_token", accessToken,
                        "refresh_token", refreshToken
                )
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal PrincipalDetails principal) {
        Member m = principal.getMember();

        return ResponseEntity.ok(
                Map.of(
                        "email", m.getEmail(),
                        "nickname", m.getNickname(),
                        "weeklyBudget", m.getWeeklyBudget(),
                        "eatoutCount", m.getEatoutCount(),
                        "mode", m.getMode()
                )
        );
    }

    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody Map<String, Object> req
    ) {
        Member m = principal.getMember();

        if (req.containsKey("weeklyBudget"))
            m.setWeeklyBudget((Integer) req.get("weeklyBudget"));

        if (req.containsKey("eatoutCount"))
            m.setEatoutCount((Integer) req.get("eatoutCount"));

        memberRepository.save(m);

        Map<String, Object> response = new HashMap<>();
        response.put("weekly_budget", m.getWeeklyBudget());
        response.put("eatout_count", m.getEatoutCount());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal PrincipalDetails principal) {
        Member m = principal.getMember();
        m.setRefreshToken(null);
        memberRepository.save(m);
        return ResponseEntity.ok("logged out");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {

        String refreshToken = body.get("refresh_token");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("refresh_token is required");
        }

        // 1) 토큰 유효성 확인
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body("Invalid refresh token");
        }

        // 2) 토큰에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getUserId(refreshToken);

        // 3) DB에 저장된 refresh token과 비교
        Member member = memberRepository.findById(userId)
                .orElse(null);

        if (member == null || member.getRefreshToken() == null ||
                !member.getRefreshToken().equals(refreshToken)) {
            return ResponseEntity.status(401).body("Refresh token mismatch");
        }

        // 4) 새 access token 발급
        String newAccessToken = jwtTokenProvider.createAccessToken(userId);

        return ResponseEntity.ok(
                Map.of("access_token", newAccessToken)
        );
    }

}