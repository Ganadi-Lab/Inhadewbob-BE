package GanadiLab.inhadewbob.domain.consume.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsumeLogResponse {
    private Long id;
    private Long memberId;
    private Integer spentAmount;
    private Integer remainingBudget;
    private String date;
    private String createdAt;
}
