package GanadiLab.inhadewbob.domain.consume.dto.request;

import lombok.Data;

@Data
public class ConsumeLogUpdateRequest {
    private Integer spentAmount;
    private Integer remainingBudget;
}
