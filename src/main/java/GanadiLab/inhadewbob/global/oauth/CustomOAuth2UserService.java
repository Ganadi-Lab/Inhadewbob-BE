package GanadiLab.inhadewbob.global.oauth;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.model.Mode;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import GanadiLab.inhadewbob.global.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub");

        Member member = memberRepository.findByProviderId(providerId)
                .orElseGet(() -> memberRepository.save(
                        Member.builder()
                                .email(email)
                                .nickname(name)
                                .provider("google")
                                .providerId(providerId)
                                .weeklyBudget(0)
                                .eatoutCount(0)
                                .mode(Mode.EASY)
                                .build()
                ));

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}