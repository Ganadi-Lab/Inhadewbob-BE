package GanadiLab.inhadewbob.domain.diet.dto.response;

import GanadiLab.inhadewbob.domain.diet.model.DietLog;
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

    public static DietLogResponse from(DietLog diet) {
        return DietLogResponse.builder()
                .id(diet.getId())
                .memberId(diet.getMember().getId())
                .menuId(diet.getMenu().getId())
                .menuName(diet.getMenu().getName())
                .price(diet.getMenu().getPrice())
                .restaurantName(diet.getMenu().getRestaurant().getName())
                .createdAt(diet.getCreatedAt().toString())
                .build();
    }
}