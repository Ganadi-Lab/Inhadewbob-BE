package GanadiLab.inhadewbob.domain.menu.service;

import GanadiLab.inhadewbob.domain.menu.dto.MenuDTO;

import java.time.LocalDate;
import java.util.List;

public interface MenuService {

    /* 식당 메뉴 랜덤 조회 */
    MenuDTO.Response getMenusByRoulette(LocalDate date, String category, Integer price, Long memberId);

    /* 추천 예산 조회 */
    MenuDTO.RecommendResponse getRecommendation(LocalDate date, Integer eatout, Long memberId);
}
