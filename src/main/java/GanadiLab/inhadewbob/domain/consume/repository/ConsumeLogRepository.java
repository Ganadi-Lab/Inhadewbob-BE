package GanadiLab.inhadewbob.domain.consume.repository;

import GanadiLab.inhadewbob.domain.consume.model.ConsumeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ConsumeLogRepository extends JpaRepository<ConsumeLog, Long> {

    // 특정 날짜의 소비 로그(하루 단위)
    Optional<ConsumeLog> findByMemberIdAndDate(Long memberId, LocalDate date);

    // 주간 소비 로그 조회 (start ~ end 사이)
    @Query("SELECT c FROM ConsumeLog c " +
            "WHERE c.member.id = :memberId " +
            "AND c.date BETWEEN :start AND :end")
    Optional<ConsumeLog> findWeeklyLog(
            @Param("memberId") Long memberId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query(value = "SELECT COALESCE(SUM(spent_amount), 0) FROM consume_log " +
            "WHERE member_id = :memberId " +
            "AND date BETWEEN :start AND :end", nativeQuery = true)
    Integer sumWeeklyConsumeLog(
            @Param("start") LocalDate start,
            @Param("end")  LocalDate end,
            @Param("memberId") Long memberId
    );
}
