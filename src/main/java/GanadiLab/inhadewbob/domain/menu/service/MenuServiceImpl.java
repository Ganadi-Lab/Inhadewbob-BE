package GanadiLab.inhadewbob.domain.menu.service;

import GanadiLab.inhadewbob.domain.consume.repository.ConsumeLogRepository;
import GanadiLab.inhadewbob.domain.diet.model.DietLog;
import GanadiLab.inhadewbob.domain.diet.repository.DietLogRepository;
import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import GanadiLab.inhadewbob.domain.menu.dto.MenuDTO;
import GanadiLab.inhadewbob.domain.menu.model.Menu;
import GanadiLab.inhadewbob.domain.menu.repository.MenuRepository;
import GanadiLab.inhadewbob.global.base.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final DietLogRepository dietLogRepository;
    private final ConsumeLogRepository consumeLogRepository;

    @Override
    public MenuDTO.Response getMenusByRoulette(LocalDate date, String category, Integer price, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 지난 주
        LocalDate lastWeekDay = date.minusWeeks(1);
        LocalDate start = DateUtil.getStartOfWeek(lastWeekDay);
        LocalDate end = DateUtil.getEndOfWeek(lastWeekDay);
        
        // 지난 주에 먹은 메뉴 리스트
        List<Long> lastWeeks = dietLogRepository.findByMemberIdAndDate(memberId, start, end).stream()
                .map(d -> d.getMenu().getId())
                .toList();

        // 랜덤 돌리기
        List<Menu> rouletteMenus = menuRepository.findMenusByRoulette(category, price, lastWeeks);

        // 랜덤 뽑기 결과
        List<Long> selectedMenus = rouletteMenus.stream()
                .map(Menu::getId)
                .toList();

        if(selectedMenus.isEmpty()) {
            selectedMenus = List.of(-1L);
        }

        // + 500 추천
        List<Menu> aroundUp = menuRepository.findMenusAround(category, price + 500, lastWeeks, selectedMenus);

        // - 500 추천
        List<Menu> aroundDown = menuRepository.findMenusAround(category, price - 500, lastWeeks, selectedMenus);

        return MenuDTO.Response.from(
                rouletteMenus.stream().map(MenuDTO.RouletteResponse::from).toList(),
                aroundUp.stream().map(MenuDTO.RouletteResponse::from).toList(),
                aroundDown.stream().map(MenuDTO.RouletteResponse::from).toList()
        );
    }

    @Override
    public MenuDTO.RecommendResponse getRecommendation(LocalDate date, Integer eatout, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        LocalDate startOfWeek = DateUtil.getStartOfWeek(date);
        LocalDate endOfWeek = DateUtil.getEndOfWeek(date);

        Integer currentSpent = consumeLogRepository.sumWeeklyConsumeLog(startOfWeek, endOfWeek, memberId);

        Integer remainingBudget = member.getWeeklyBudget() - currentSpent;
        Integer remainingEatoutCount = member.getEatoutCount() - eatout + 1;
        Integer recommendation = 0;

        if(remainingBudget <=0 || remainingEatoutCount <= 0) {
            return MenuDTO.RecommendResponse.from(recommendation);
        }

        recommendation = remainingBudget / remainingEatoutCount;

        return MenuDTO.RecommendResponse.from(recommendation);
    }
}
