package GanadiLab.inhadewbob.domain.diet.dto.request;

import lombok.Data;

@Data
public class DietLogCreateRequest {
    private Long memberId;
    private Long menuId;
}
