package GanadiLab.inhadewbob.domain.diet.dto.response;

import GanadiLab.inhadewbob.domain.diet.model.DietLog;
import GanadiLab.inhadewbob.domain.manualDietDetail.model.ManualDietDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LatestDietLogResponse {
    private Long id;
    private String restaurantName;
    private String menuName;
    private Integer price;

    public static LatestDietLogResponse from(DietLog dietLog) {

        ManualDietDetail detail = dietLog.getManualDietDetail();

        if (detail != null) {

            return LatestDietLogResponse.builder()
                    .id(dietLog.getId())
                    .restaurantName(detail.getRestaurantName()) // 수기 입력 식당
                    .menuName(detail.getMenuName())             // 수기 입력 메뉴명
                    .price(detail.getPrice())                   // 수기 입력 가격
                    .build();
        }

        return LatestDietLogResponse.builder()
                .id(dietLog.getId())
                .restaurantName(dietLog.getMenu().getRestaurant().getName())
                .menuName(dietLog.getMenu().getName())
                .price(dietLog.getMenu().getPrice())
                .build();
    }
}
