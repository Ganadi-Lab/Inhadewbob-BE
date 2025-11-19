package GanadiLab.inhadewbob.domain.menu.dto;

import GanadiLab.inhadewbob.domain.menu.model.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MenuDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        List<RouletteResponse> rouletteResponses;
        List<AroundUpResponse> aroundUpResponses;       // 최대 2개
        List<AroundDownResponse> aroundDownResponses;   // 최대 2개

        public static Response from(List<RouletteResponse> rouletteResponses,
                                    List<AroundUpResponse> aroundUpResponse,
                                    List<AroundDownResponse> aroundDownResponse) {

            return Response.builder()
                    .rouletteResponses(rouletteResponses)
                    .aroundUpResponses(aroundUpResponse)
                    .aroundDownResponses(aroundDownResponse)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouletteResponse {
        private String restaurantName;
        private String location;
        private Long menuId;
        private String menuName;
        private String category;
        private Integer price;
        private String imagePath;

        public static RouletteResponse from(Menu menu) {

            return RouletteResponse.builder()
                    .restaurantName(menu.getRestaurant().getName())
                    .location(menu.getRestaurant().getName())
                    .menuId(menu.getId())
                    .menuName(menu.getName())
                    .category(menu.getCategory())
                    .price(menu.getPrice())
                    .imagePath(menu.getStoredFileName())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AroundUpResponse {
        private String restaurantName;
        private String location;
        private Long menuId;
        private String menuName;
        private String category;
        private Integer price;
        private String imagePath;

        public static AroundUpResponse from(Menu menu) {

            return AroundUpResponse.builder()
                    .restaurantName(menu.getRestaurant().getName())
                    .location(menu.getRestaurant().getName())
                    .menuId(menu.getId())
                    .menuName(menu.getName())
                    .category(menu.getCategory())
                    .price(menu.getPrice())
                    .imagePath(menu.getStoredFileName())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AroundDownResponse {
        private String restaurantName;
        private String location;
        private Long menuId;
        private String menuName;
        private String category;
        private Integer price;
        private String imagePath;

        public static AroundDownResponse from(Menu menu) {

            return AroundDownResponse.builder()
                    .restaurantName(menu.getRestaurant().getName())
                    .location(menu.getRestaurant().getName())
                    .menuId(menu.getId())
                    .menuName(menu.getName())
                    .category(menu.getCategory())
                    .price(menu.getPrice())
                    .imagePath(menu.getStoredFileName())
                    .build();
        }
    }
}
