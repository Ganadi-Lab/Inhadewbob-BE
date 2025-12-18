package NotFound.next_campus.global.auth.token.service;

import NotFound.next_campus.global.auth.token.dto.request.LoginRequest;
import NotFound.next_campus.global.auth.token.exception.TokenException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class TokenService {
    private final AuthenticationManager authenticationManager;
    public final JwtTokenProvider tokenProvider; //일단 public으로 바꿈
    private final MemberAuthService memberAuthService;


    public TokenService(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, MemberAuthService memberAuthService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.memberAuthService = memberAuthService;
    }

    @Transactional
    public LoginTokens login(LoginRequest req) {
        // 1) 인증 수행 (UserDetailsService와 PasswordEncoder로 검증)
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getStudentId(), req.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);


        // 2) 토큰 생성
        String role = auth.getAuthorities().iterator().next().getAuthority();
        String access = tokenProvider.createAccessToken(req.getStudentId(), role);
        String refresh = tokenProvider.createRefreshToken(req.getStudentId());


        // 3) DB에 refresh 저장
        LocalDateTime expiry = LocalDateTime.ofInstant(Instant.now().plusMillis(tokenProvider.refreshTokenMillis), ZoneOffset.UTC);
        memberAuthService.saveRefreshToken(req.getStudentId(), refresh, expiry);

        return new LoginTokens(access, refresh);
    }

    @Transactional
    public LoginTokens refresh(String refreshToken) {
        // 토큰 자체 유효성 검증
        if (refreshToken == null || !tokenProvider.validateToken(refreshToken)) throw new TokenException("Invalid refresh token");
        String studentId = tokenProvider.getSubjectFromToken(refreshToken);


        // DB에 저장된 토큰과 비교
        String stored = memberAuthService.getRefreshToken(Long.valueOf(studentId));
        if (stored == null || !stored.equals(refreshToken)) throw new TokenException("Refresh token not found or mismatched");


        // 만료 검사 (DB에 저장된 expiry와 비교)
        LocalDateTime expiry = memberAuthService.getRefreshExpiry(Long.valueOf(studentId));
        if (expiry == null || LocalDateTime.now(ZoneOffset.UTC).isAfter(expiry)) {
            memberAuthService.clearRefreshToken(Long.valueOf(studentId));
            throw new TokenException("Refresh token이 만료되었습니다.");
        }


        String role = memberAuthService.getRoleByStudentId(Long.valueOf(studentId));

        // 새 토큰 발급 및 DB 갱신
        String newAccess = tokenProvider.createAccessToken(Long.valueOf(studentId), role);
        // String newRefresh = tokenProvider.createRefreshToken(Long.valueOf(studentId));
        // LocalDateTime newExpiry = LocalDateTime.ofInstant(Instant.now().plusMillis(tokenProvider.refreshTokenMillis), ZoneOffset.UTC);
        // memberAuthService.saveRefreshToken(Long.valueOf(studentId), newRefresh, newExpiry);


        return new LoginTokens(newAccess, stored);
    }


    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null) return;
        if (!tokenProvider.validateToken(refreshToken)) return;
        String studentId = tokenProvider.getSubjectFromToken(refreshToken);
        memberAuthService.clearRefreshToken(Long.valueOf(studentId));
    }


    // 내부 토큰 보관용 DTO
    public static class LoginTokens {
        public final String accessToken;
        public final String refreshToken;
        public LoginTokens(String a, String r) { this.accessToken = a; this.refreshToken = r; }
    }
}