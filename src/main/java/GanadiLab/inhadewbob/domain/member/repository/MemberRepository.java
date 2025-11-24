package GanadiLab.inhadewbob.domain.member.repository;

import GanadiLab.inhadewbob.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // providerId(sub)로 회원 찾기
    Optional<Member> findByProviderId(String providerId);

    Optional<Member> findByEmail(String email);
}
