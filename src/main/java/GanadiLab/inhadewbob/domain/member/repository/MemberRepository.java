package GanadiLab.inhadewbob.domain.member.repository;

import GanadiLab.inhadewbob.domain.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
