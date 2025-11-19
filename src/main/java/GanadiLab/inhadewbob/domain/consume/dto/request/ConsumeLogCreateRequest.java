package GanadiLab.inhadewbob.domain.consume.dto.request;

import lombok.Data;

@Data
public class ConsumeLogCreateRequest {
    private Long memberId;
    private Integer spentAmount;
    private Integer remainingBudget;
    private String date;   // YYYY-MM-DD
}
