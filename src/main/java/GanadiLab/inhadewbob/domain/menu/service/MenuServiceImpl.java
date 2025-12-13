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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;
    private final DietLogRepository dietLogRepository;
    private final ConsumeLogRepository consumeLogRepository;

    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @Override
    public MenuDTO.Response getMenusByRoulette(LocalDate date, List<String> categories, Integer price, Member member) {

        // 지난 주
        LocalDate lastWeekDay = date.minusWeeks(1);
        LocalDateTime start = DateUtil.getStartOfWeek(lastWeekDay).atStartOfDay();
        LocalDateTime end = DateUtil.getEndOfWeek(lastWeekDay).plusDays(1).atStartOfDay();
        
        // 지난 주에 먹은 메뉴 리스트
        List<Long> lastWeeks = dietLogRepository.findByMemberIdAndDate(member.getId(), start, end).stream()
                .map(d -> d.getMenu().getId())
                .toList();

        if(lastWeeks.isEmpty()) {
            lastWeeks = List.of(-1L);
        }

        // 랜덤 돌리기
        List<Menu> rouletteMenus = menuRepository.findMenusByRoulette(categories, price, lastWeeks);

        // 랜덤 뽑기 결과
        List<Long> selectedMenus = rouletteMenus.stream()
                .map(Menu::getId)
                .toList();

        if(selectedMenus.isEmpty()) {
            selectedMenus = List.of(-1L);
        }

        // + 500 추천
        List<Menu> aroundUp = menuRepository.findMenusAround(categories, price + 500, lastWeeks, selectedMenus);

        // - 500 추천
        List<Menu> aroundDown = menuRepository.findMenusAround(categories, price - 500, lastWeeks, selectedMenus);

        return MenuDTO.Response.from(
                rouletteMenus.stream().map(MenuDTO.RouletteResponse::from).toList(),
                aroundUp.stream().map(MenuDTO.RouletteResponse::from).toList(),
                aroundDown.stream().map(MenuDTO.RouletteResponse::from).toList()
        );
    }

    @Override
    public MenuDTO.RecommendResponse getRecommendation(LocalDate date, Member member) {

        Long memberId = member.getId();

        LocalDateTime start = DateUtil.getStartOfWeek(date).atStartOfDay();
        LocalDateTime end = DateUtil.getEndOfWeek(date).plusDays(1).atStartOfDay();

        Integer currentSpent = consumeLogRepository.sumWeeklyConsumeLog(start.toLocalDate(), end.toLocalDate(), memberId);
        Integer eatout = dietLogRepository.countDietLogMyMemberIdAndDate(start, end, memberId);

        Integer remainingBudget = member.getWeeklyBudget() - currentSpent;
        Integer remainingEatoutCount = member.getEatoutCount() - eatout;
        Integer recommendation = 0;

        if(remainingBudget <=0 || remainingEatoutCount <= 0) {
            return MenuDTO.RecommendResponse.from(recommendation);
        }

        recommendation = remainingBudget / remainingEatoutCount;

        return MenuDTO.RecommendResponse.from(recommendation);
    }

    @Override
    public void saveMenuImage(Long menuId, MultipartFile file) {

        Menu menu = menuRepository.findById(menuId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 메뉴입니다.")
        );

        if (file.isEmpty()) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다.");
        }

        String originalFileName = file.getOriginalFilename();
        String storedFileName = UUID.randomUUID() + "_" + originalFileName;

        String path = UPLOAD_DIR + storedFileName;

        try {
            File destination = new File(path);
            destination.getParentFile().mkdirs();
            file.transferTo(destination);
        } catch (IOException e) {
            throw new IllegalArgumentException("파일 저장 실패");
        }

        menu.setOriginalFileName(originalFileName);
        menu.setStoredFileName(storedFileName);
    }
}
