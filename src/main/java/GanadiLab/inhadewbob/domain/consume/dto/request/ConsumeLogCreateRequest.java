package GanadiLab.inhadewbob.domain.consume.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ConsumeLogCreateRequest {
    private Long memberId;
    //private Integer spentAmount;
    private Integer remainingBudget;
    private LocalDate date;
}
