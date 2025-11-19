package GanadiLab.inhadewbob.domain.consume.repository;

import GanadiLab.inhadewbob.domain.consume.model.ConsumeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConsumeLogRepository extends JpaRepository<ConsumeLog, Long> {

    // 특정 날짜의 소비 로그(하루 단위)
    Optional<ConsumeLog> findByMemberIdAndDate(Long memberId, String date);

    // 주간 소비 로그 조회 (start ~ end 사이)
    @Query("SELECT c FROM ConsumeLog c " +
            "WHERE c.member.id = :memberId " +
            "AND c.date BETWEEN :start AND :end")
    Optional<ConsumeLog> findWeeklyLog(
            @Param("memberId") Long memberId,
            @Param("start") String start,
            @Param("end") String end
    );
}
