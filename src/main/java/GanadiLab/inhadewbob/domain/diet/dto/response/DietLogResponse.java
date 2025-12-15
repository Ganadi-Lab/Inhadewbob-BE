package GanadiLab.inhadewbob.domain.diet.dto.response;

import GanadiLab.inhadewbob.domain.diet.model.DietLog;
import GanadiLab.inhadewbob.domain.manualDietDetail.model.ManualDietDetail;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

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

        ManualDietDetail detail = diet.getManualDietDetail();

        if (detail != null) {

            return DietLogResponse.builder()
                    .id(diet.getId())
                    .memberId(diet.getMember().getId())
                    .menuId(diet.getMenu().getId())          // 10001
                    .menuName(detail.getMenuName())          // 수기 입력 메뉴명
                    .price(detail.getPrice())                // 수기 입력 가격
                    .restaurantName(detail.getRestaurantName())
                    .createdAt(
                            LocalDateTime.of(
                                    detail.getDate(),
                                    detail.getTime()
                            ).toString()
                    )
                    .build();
        }

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