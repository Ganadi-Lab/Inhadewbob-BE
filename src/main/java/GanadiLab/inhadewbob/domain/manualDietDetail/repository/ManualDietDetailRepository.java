package GanadiLab.inhadewbob.domain.manualDietDetail.repository;

import GanadiLab.inhadewbob.domain.manualDietDetail.model.ManualDietDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import GanadiLab.inhadewbob.domain.diet.model.DietLog;

public interface ManualDietDetailRepository
        extends JpaRepository<ManualDietDetail, Long> {

    Optional<ManualDietDetail> findByDietLog(DietLog dietLog);
}

