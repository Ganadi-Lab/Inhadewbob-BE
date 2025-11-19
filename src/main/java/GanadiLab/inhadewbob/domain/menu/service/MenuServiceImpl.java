package GanadiLab.inhadewbob.domain.menu.service;

import GanadiLab.inhadewbob.domain.menu.dto.MenuDTO;
import GanadiLab.inhadewbob.domain.menu.model.Menu;
import GanadiLab.inhadewbob.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public MenuDTO.Response getMenusByRoulette(LocalDate date, String category, Integer price) {

        /* 전주에 먹은 메뉴 조회 하는 코드 추가 */
        List<Long> lastWeeks = List.of(-1L);

        // 랜덤 돌리기
        List<Menu> rouletteMenus = menuRepository.findMenusByRoulette(category, price, lastWeeks);

        // 랜덤 뽑기 결과
        List<Long> selectedMenus = rouletteMenus.stream()
                .map(Menu::getId)
                .toList();

        // + 500 추천
        List<Menu> aroundUp = menuRepository.findMenusAround(category, price + 500, lastWeeks, selectedMenus);

        // - 500 추천
        List<Menu> aroundDown = menuRepository.findMenusAround(category, price - 500, lastWeeks, selectedMenus);

        return MenuDTO.Response.from(
                rouletteMenus.stream().map(MenuDTO.RouletteResponse::from).toList(),
                aroundUp.stream().map(MenuDTO.AroundUpResponse::from).toList(),
                aroundDown.stream().map(MenuDTO.AroundDownResponse::from).toList()
        );
    }
}
