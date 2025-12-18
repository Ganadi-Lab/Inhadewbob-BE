package GanadiLab.inhadewbob.global.newAuth.token.api;

import GanadiLab.inhadewbob.global.auth.PrincipalDetails;
import GanadiLab.inhadewbob.global.newAuth.token.dto.request.LoginRequest;
import GanadiLab.inhadewbob.global.newAuth.token.dto.request.RefreshRequest;
import GanadiLab.inhadewbob.global.newAuth.token.dto.request.UpdateProfileRequest;
import GanadiLab.inhadewbob.global.newAuth.token.dto.response.LoginResponse;
import GanadiLab.inhadewbob.global.newAuth.token.dto.response.ProfileResponse;
import GanadiLab.inhadewbob.global.newAuth.token.service.MemberAuthService;
import GanadiLab.inhadewbob.global.newAuth.token.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class TokenController {

    private final TokenService tokenService;
    private final MemberAuthService memberAuthService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        TokenService.LoginTokens tokens = tokenService.login(request);
        return ResponseEntity.ok(
                new LoginResponse(tokens.accessToken(), tokens.refreshToken())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @RequestBody RefreshRequest request
    ) {
        TokenService.LoginTokens tokens =
                tokenService.refresh(request.getRefreshToken());

        return ResponseEntity.ok(
                new LoginResponse(tokens.accessToken(), tokens.refreshToken())
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody RefreshRequest request
    ) {
        tokenService.logout(request.getRefreshToken());
        return ResponseEntity.ok().build();
    }

    // 내 정보 조회
    @GetMapping("/profile")
    public ProfileResponse me(
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return memberAuthService.getMyProfile(principal);
    }

    // 내 정보 수정
    @PatchMapping("/profile")
    public ProfileResponse update(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestBody UpdateProfileRequest request
    ) {
        return memberAuthService.updateMyProfile(
                principal,
                request.eatoutCount(),
                request.weeklyBudget()
        );
    }
}
