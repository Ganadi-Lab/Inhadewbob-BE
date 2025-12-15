package GanadiLab.inhadewbob.domain.diet.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DietLogCreateRequest {
    private Long menuId;   // null이면 수기입력

    private LocalDate date;
    private LocalTime time;

    // 수기입력 전용
    private String restaurantName;
    private String menuName;
    private Integer price;
}
