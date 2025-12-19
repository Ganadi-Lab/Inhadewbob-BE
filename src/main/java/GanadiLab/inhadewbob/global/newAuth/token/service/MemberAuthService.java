package GanadiLab.inhadewbob.global.newAuth.token.service;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.model.Mode;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import GanadiLab.inhadewbob.global.auth.PrincipalDetails;
import GanadiLab.inhadewbob.global.newAuth.token.dto.response.ProfileResponse;
import GanadiLab.inhadewbob.global.newAuth.token.dto.response.SignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class MemberAuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new PrincipalDetails(member, null);
    }

    // refresh token 저장
    public void saveRefreshToken(Long memberId, String refreshToken) {
        memberRepository.findById(memberId).ifPresent(m -> {
            m.setRefreshToken(refreshToken);
        });
    }

    public String getRefreshToken(Long memberId) {
        return memberRepository.findById(memberId)
                .map(Member::getRefreshToken)
                .orElse(null);
    }

    public void clearRefreshToken(Long memberId) {
        memberRepository.findById(memberId).ifPresent(m -> {
            m.setRefreshToken(null);
        });
    }

    @Transactional
    public ProfileResponse getMyProfile(PrincipalDetails principal) {
        return ProfileResponse.from(principal.getMember());
    }

    @Transactional
    public ProfileResponse updateMyProfile(
            PrincipalDetails principal,
            Integer eatoutCount,
            Integer weeklyBudget
    ) {
        Member member = principal.getMember();

        if (eatoutCount != null) {
            member.setEatoutCount(eatoutCount);
        }
        if (weeklyBudget != null) {
            member.setWeeklyBudget(weeklyBudget);
        }

        return ProfileResponse.from(member);
    }

    @Transactional
    public SignupResponse signup(String email, String password, String nickname) {

        // 이메일 중복 체크
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .eatoutCount(0)
                .provider("local")
                .providerId("sub")  // 실제 OAuth sub
                .mode(Mode.EASY)
                .build();

        Member saved = memberRepository.save(member);

        return new SignupResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getNickname()
        );
    }

}
