package GanadiLab.inhadewbob.domain.diet.dto.response;

import GanadiLab.inhadewbob.domain.diet.model.DietLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestDietLogResponse {
    private String restaurantName;
    private String menuName;
    private Integer price;

    public static LatestDietLogResponse from(DietLog dietLog) {

        return LatestDietLogResponse.builder()
                .restaurantName(dietLog.getMenu().getRestaurant().getName())
                .menuName(dietLog.getMenu().getName())
                .price(dietLog.getMenu().getPrice())
                .build();
    }
}
