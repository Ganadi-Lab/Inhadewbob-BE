package NotFound.next_campus.global.auth.token.service;

import NotFound.next_campus.domain.member.model.Member;
import NotFound.next_campus.domain.member.repository.MemberRepository;
import NotFound.next_campus.global.auth.token.dto.response.ProfileResponse;
import NotFound.next_campus.global.auth.user.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

/**
 * 기존 Member 엔티티/레포지토리와 연결하는 서비스
 * - UserDetailsService 구현으로 Spring Security가 사용
 * - refresh token 저장/조회/삭제 유틸 제공
 */
@Service
public class MemberAuthService implements UserDetailsService {
    private final MemberRepository memberRepository;


    public MemberAuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    // Spring Security가 인증 시 호출하는 메서드
    @Override
    public CustomUserDetails loadUserByUsername(String studentIdStr) throws UsernameNotFoundException {
        Long studentId = Long.valueOf(studentIdStr);
        Member member = memberRepository.findByStudentId(studentId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + studentId));

        // CustomUserDetails 반환
        return new CustomUserDetails(member);
    }


    // refresh token 저장
    public void saveRefreshToken(Long studentId, String refreshToken, LocalDateTime expiry) {
        memberRepository.findByStudentId(studentId).ifPresent(m -> {
            m.setRefreshToken(refreshToken);
            m.setRefreshExpiry(expiry);
            // memberRepository.save(m);
        });
    }


    public String getRefreshToken(Long studentId) {
        return memberRepository.findByStudentId(studentId).map(Member::getRefreshToken).orElse(null);
    }


    public LocalDateTime getRefreshExpiry(Long studentId) {
        return memberRepository.findByStudentId(studentId).map(Member::getRefreshExpiry).orElse(null);
    }


    public void clearRefreshToken(Long studentId) {
        Optional<Member> opt = memberRepository.findByStudentId(studentId);
        if (opt.isEmpty()) return;
        Member m = opt.get();
        m.setRefreshToken(null);
        m.setRefreshExpiry(null);
        //memberRepository.save(m);
    }


    // refreshExpiry 문자열을 Instant로 파싱 (null-safe)
    public Instant parseExpiry(String expiryIso) {
        if (expiryIso == null) return null;
        try {
            return Instant.parse(expiryIso);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public String getRoleByStudentId(Long studentId) {
        return memberRepository.findByStudentId(studentId)
                .map(member -> member.getRole().name())
                .orElse("USER");  // 기본값 USER
    }

    public ProfileResponse getProfile(CustomUserDetails userDetails) {
        Member member = userDetails.getMember();

        return new ProfileResponse(
                member.getStudentId(),
                member.getName(),
                member.getEmail(),
                member.getDepartment(),
                member.getRole().name()
        );
    }
}