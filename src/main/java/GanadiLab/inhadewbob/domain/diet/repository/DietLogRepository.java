package GanadiLab.inhadewbob.domain.diet.repository;

import GanadiLab.inhadewbob.domain.diet.model.DietLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.time.LocalDateTime;

public interface DietLogRepository extends JpaRepository<DietLog, Long> {
    List<DietLog> findByMemberId(Long memberId);

    // 특정 유저, 특정 날짜 조회 (createdAt 기준)
    @Query("SELECT d FROM DietLog d " +
            "WHERE d.member.id = :memberId " +
            "AND d.createdAt BETWEEN :start AND :end")
    List<DietLog> findByMemberIdAndDate(
            @Param("memberId") Long memberId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("SELECT COUNT(d) FROM DietLog d " +
            "WHERE d.member.id = :memberId " +
            "AND d.createdAt >= :start " +
            "AND d.createdAt < :end")
    Integer countDietLogMyMemberIdAndDate(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("memberId") Long memberId
    );
}
