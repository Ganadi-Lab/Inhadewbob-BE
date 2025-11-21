package GanadiLab.inhadewbob.domain.diet.service;

import GanadiLab.inhadewbob.domain.consume.model.ConsumeLog;
import GanadiLab.inhadewbob.domain.diet.dto.request.DietLogCreateRequest;
import GanadiLab.inhadewbob.domain.diet.dto.response.DietLogResponse;
import GanadiLab.inhadewbob.domain.diet.model.DietLog;
import GanadiLab.inhadewbob.domain.diet.repository.DietLogRepository;
import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import GanadiLab.inhadewbob.domain.menu.model.Menu;
import GanadiLab.inhadewbob.domain.menu.repository.MenuRepository;
import GanadiLab.inhadewbob.domain.consume.repository.ConsumeLogRepository;
import java.time.LocalDate;
import GanadiLab.inhadewbob.global.base.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DietLogServiceImpl implements  DietLogService {

    private final DietLogRepository dietLogRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final ConsumeLogRepository consumeLogRepository;

    @Override
    public DietLogResponse create(DietLogCreateRequest request) {

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Menu menu = menuRepository.findById(request.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다."));

        // 날짜
        LocalDate today = LocalDate.now();
        String todayStr = today.toString();

        // 이번 주의 범위 계산
        LocalDate startOfWeek = DateUtil.getStartOfWeek(today);
        LocalDate endOfWeek = DateUtil.getEndOfWeek(today);

        // 이번 주 소비 기록 조회
        ConsumeLog weeklyLog = consumeLogRepository.findWeeklyLog(
                member.getId(), startOfWeek, endOfWeek
        ).orElse(null);

        // 없으면 새로 생성 —> 이번 주 예산 초기값은 member.profile 등에 저장된 기본 예산이 있다고 가정
        if (weeklyLog == null) {
            weeklyLog = consumeLogRepository.save(
                    ConsumeLog.builder()
                            .member(member)
                            .date(today)
                            .spentAmount(0)
                            .remainingBudget(member.getWeeklyBudget())  // 예산 기본값
                            .build()
            );
        }

        // 가격차감
        int price = menu.getPrice();
        int updatedSpent = weeklyLog.getSpentAmount() + price;
        int updatedRemaining = weeklyLog.getRemainingBudget() - price;

        weeklyLog = consumeLogRepository.save(
                ConsumeLog.builder()
                        .id(weeklyLog.getId())
                        .member(weeklyLog.getMember())
                        .date(weeklyLog.getDate())
                        .spentAmount(updatedSpent)
                        .remainingBudget(updatedRemaining)
                        .build()
        );

        DietLog dietLog = dietLogRepository.save(
                DietLog.builder()
                        .member(member)
                        .menu(menu)
                        .build()
        );

        return toResponse(dietLog);
    }

    @Override
    public List<DietLogResponse> getByMember(Long memberId) {
        return dietLogRepository.findByMemberId(memberId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private DietLogResponse toResponse(DietLog diet) {
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