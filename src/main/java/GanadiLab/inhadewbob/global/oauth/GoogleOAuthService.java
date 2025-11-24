package GanadiLab.inhadewbob.global.oauth;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.model.Mode;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import GanadiLab.inhadewbob.global.oauth.dto.GoogleTokenResponse;
import GanadiLab.inhadewbob.global.oauth.dto.GoogleUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final MemberRepository memberRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    // 1) code로 access token 요청
    public String getAccessToken(String code) {

        String url = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "code=" + code +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&grant_type=authorization_code";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<GoogleTokenResponse> response =
                new RestTemplate().postForEntity(url, request, GoogleTokenResponse.class);

        return response.getBody().getAccessToken();
    }

    // 2) access_token으로 userinfo
    public GoogleUserInfo getUserInfo(String accessToken) {

        String url = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<GoogleUserInfo> response =
                new RestTemplate().exchange(url, HttpMethod.GET, request, GoogleUserInfo.class);

        return response.getBody();
    }

    // 3) Member 조회/생성
    public Member findOrCreate(GoogleUserInfo info) {

        Optional<Member> memberOpt = memberRepository.findByProviderId(info.getSub());
        if (memberOpt.isPresent()) return memberOpt.get();

        return memberRepository.save(Member.builder()
                .email(info.getEmail())
                .nickname(info.getName())
                .provider("google")
                .providerId(info.getSub())
                .weeklyBudget(0)
                .eatoutCount(0)
                .mode(Mode.EASY)
                .build());
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
