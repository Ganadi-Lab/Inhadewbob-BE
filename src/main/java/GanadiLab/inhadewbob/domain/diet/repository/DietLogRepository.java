package GanadiLab.inhadewbob.domain.diet.repository;

import GanadiLab.inhadewbob.domain.diet.model.DietLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DietLogRepository extends JpaRepository<DietLog, Long> {
    List<DietLog> findByMemberId(Long memberId);
}
