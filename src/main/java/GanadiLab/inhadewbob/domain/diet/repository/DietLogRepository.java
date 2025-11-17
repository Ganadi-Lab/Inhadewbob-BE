package GanadiLab.inhadewbob.domain.diet_log.repository;

import GanadiLab.inhadewbob.domain.diet_log.model.DietLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietLogRepository extends JpaRepository<DietLog, Long> {
}
