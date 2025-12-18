package GanadiLab.inhadewbob.global.newAuth.token.service;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import GanadiLab.inhadewbob.global.auth.PrincipalDetails;
import GanadiLab.inhadewbob.global.newAuth.token.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MemberAuthService implements UserDetailsService {

    private final MemberRepository memberRepository;

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
}
