package GanadiLab.inhadewbob.domain.diet.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DietLogResponse {
    private Long id;
    private Long memberId;
    private Long menuId;
    private String menuName;
    private Integer price;
    private String restaurantName;
    private String createdAt;
}