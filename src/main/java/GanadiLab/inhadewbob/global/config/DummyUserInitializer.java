package GanadiLab.inhadewbob.global.config;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.model.Mode;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DummyUserInitializer {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDummyUsers(MemberRepository memberRepository) {
        return args -> {

            // 테스트 USER
            if (memberRepository.findByEmail("test@test.com").isEmpty()) {
                Member user = Member.builder()
                        .email("test@test.com")
                        .password(passwordEncoder.encode("1234"))
                        .nickname("테스트유저")
                        .provider("local")
                        .providerId("local-test@test.com")
                        .eatoutCount(0)
                        .mode(Mode.EASY)
                        .build();

                memberRepository.save(user);
                System.out.println("✅ Dummy USER created");
            } else {
                System.out.println("ℹ Dummy USER already exists");
            }

        };
    }
}
