package GanadiLab.inhadewbob.domain.consume.dto.response;

import GanadiLab.inhadewbob.domain.consume.model.ConsumeLog;
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

    public static ConsumeLogResponse from(ConsumeLog log) {
        return ConsumeLogResponse.builder()
                .id(log.getId())
                .memberId(log.getMember().getId())
                .spentAmount(log.getSpentAmount())
                .remainingBudget(log.getRemainingBudget())
                .date(log.getDate().toString())
                .createdAt(log.getCreatedAt().toString())
                .build();
    }
}
