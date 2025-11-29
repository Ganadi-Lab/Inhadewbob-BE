package GanadiLab.inhadewbob.domain.menu.service;

import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.menu.dto.MenuDTO;

import java.time.LocalDate;
import java.util.List;

public interface MenuService {

    /* 식당 메뉴 랜덤 조회 */
    MenuDTO.Response getMenusByRoulette(LocalDate date, List<String> categories, Integer price, Member member);

    /* 추천 예산 조회 */
    MenuDTO.RecommendResponse getRecommendation(LocalDate date, Member member);
}
