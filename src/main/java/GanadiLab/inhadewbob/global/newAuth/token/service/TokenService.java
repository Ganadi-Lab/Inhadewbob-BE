package GanadiLab.inhadewbob.global.newAuth.token.service;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.global.auth.PrincipalDetails;
import GanadiLab.inhadewbob.global.jwt.JwtTokenProvider;
import GanadiLab.inhadewbob.global.newAuth.token.dto.request.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final MemberAuthService memberAuthService;

    @Transactional
    public LoginTokens login(LoginRequest req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        PrincipalDetails principal = (PrincipalDetails) auth.getPrincipal();
        Member member = principal.getMember();

        String accessToken = tokenProvider.createAccessToken(member.getId());
        String refreshToken = tokenProvider.createRefreshToken(member.getId());

        memberAuthService.saveRefreshToken(member.getId(), refreshToken);

        return new LoginTokens(accessToken, refreshToken);
    }

    @Transactional
    public LoginTokens refresh(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken))
            throw new RuntimeException("Invalid refresh token");

        Long memberId = tokenProvider.getMemberIdFromToken(refreshToken);

        String stored = memberAuthService.getRefreshToken(memberId);
        if (stored == null || !stored.equals(refreshToken))
            throw new RuntimeException("Refresh token mismatch");

        String newAccess = tokenProvider.createAccessToken(memberId);

        return new LoginTokens(newAccess, stored);
    }

    @Transactional
    public void logout(String refreshToken) {
        if (!tokenProvider.validateToken(refreshToken)) return;

        Long memberId = tokenProvider.getMemberIdFromToken(refreshToken);
        memberAuthService.clearRefreshToken(memberId);
    }

    public record LoginTokens(String accessToken, String refreshToken) {}
}
