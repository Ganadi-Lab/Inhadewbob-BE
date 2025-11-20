package GanadiLab.inhadewbob.domain.consume.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeStatusResponse {
    private Integer budget;
    private Integer thisWeekSpent;
    private Integer differenceFromLastWeekSpent;
}