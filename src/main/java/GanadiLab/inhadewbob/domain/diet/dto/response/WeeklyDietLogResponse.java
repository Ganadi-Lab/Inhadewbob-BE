package GanadiLab.inhadewbob.domain.diet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyDietLogResponse {
    private LocalDate date;
    private Integer count;      // 외식 횟수
}
