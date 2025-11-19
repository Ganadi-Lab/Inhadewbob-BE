package GanadiLab.inhadewbob.domain.consume.dto.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeStatResponse {
    private Integer currentMonth;       // 어느 달의 통계인지
    private Integer currentWeek;        // 몇 주차의 통계인지
    private Integer thisWeekSpent;      // 이번주
    private Integer lastWeekSpent;      // 지난주
    private Integer twoWeeksAgoSpent;   // 지지난주
}
