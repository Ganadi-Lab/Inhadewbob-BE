package GanadiLab.inhadewbob.domain.consume.service;

import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogCreateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogUpdateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeLogResponse;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeStatResponse;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeStatusResponse;
import GanadiLab.inhadewbob.domain.consume.model.ConsumeLog;
import GanadiLab.inhadewbob.domain.consume.repository.ConsumeLogRepository;
import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import GanadiLab.inhadewbob.global.base.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;

@Service
@RequiredArgsConstructor
public class ConsumeLogServiceImpl implements ConsumeLogService{

    private final ConsumeLogRepository consumeLogRepository;
    private final MemberRepository memberRepository;

    // 등록
    @Override
    public ConsumeLogResponse createConsumeLog(ConsumeLogCreateRequest req) {

        Member member = memberRepository.findById(req.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        ConsumeLog saved = consumeLogRepository.save(
                ConsumeLog.builder()
                        .member(member)
                        .remainingBudget(req.getRemainingBudget())
                        .date(req.getDate())
                        .build()
        );

        return ConsumeLogResponse.from(saved);
    }

    @Override
    public ConsumeLogResponse updateConsumeLog(Long id, ConsumeLogUpdateRequest req) {

        ConsumeLog log = consumeLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("소비 기록이 존재하지 않습니다."));

        // null 체크 후 값만 업데이트
        if (req.getSpentAmount() != null) {
            int oldSpent = log.getSpentAmount();
            int newSpent = req.getSpentAmount();

            int diff = newSpent - oldSpent; // 변화량
            log.setSpentAmount(newSpent);

            // 사용한 금액 수정 시 남은 예산 업데이트
            log.setRemainingBudget(log.getRemainingBudget() - diff);
        }

        if (req.getRemainingBudget() != null) {
            log.setRemainingBudget(req.getRemainingBudget());
        }

        consumeLogRepository.save(log);

        return ConsumeLogResponse.from(log);
    }

    @Override
    public ConsumeStatusResponse getConsumeStatus(LocalDate today, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Integer budget = member.getWeeklyBudget();

        // 지난주
        LocalDate lastWeekDay = today.minusWeeks(1);

        Integer thisWeekSpent = sumWeeklySpent(today, memberId);
        Integer lastWeekSpent = sumWeeklySpent(lastWeekDay, memberId);

        return ConsumeStatusResponse.builder()
                .budget(budget)
                .thisWeekSpent(thisWeekSpent)
                .differenceFromLastWeekSpent(thisWeekSpent - lastWeekSpent)
                .build();
    }

    // 이번 주, 지난 주, 지지난 주 소비 통계
    @Override
    public ConsumeStatResponse getConsumeStats(LocalDate today, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        // 현재 달
        int currentMonth = today.getMonthValue();

        // 이번 달의 몇주차인지
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
        int currentWeek = today.get(weekFields.weekOfMonth());

        // 지난 주
        LocalDate lastWeekDay = today.minusWeeks(1);
        // 지지난 주
        LocalDate twoWeeksAgoDay = lastWeekDay.minusWeeks(1);

        Integer thisWeekSpent = sumWeeklySpent(today, memberId);
        Integer lastWeekSpent = sumWeeklySpent(lastWeekDay, memberId);
        Integer twoWeekSpent = sumWeeklySpent(twoWeeksAgoDay, memberId);

        return ConsumeStatResponse.builder()
                .currentMonth(currentMonth)
                .currentWeek(currentWeek)
                .thisWeekSpent(thisWeekSpent)
                .lastWeekSpent(lastWeekSpent)
                .twoWeeksAgoSpent(twoWeekSpent)
                .build();
    }

    private Integer sumWeeklySpent(LocalDate date, Long memberId) {
        
        // 주차 날짜 범위 계산
        LocalDate startOfWeek = DateUtil.getStartOfWeek(date);
        LocalDate endOfWeek = DateUtil.getEndOfWeek(date);

        return consumeLogRepository.sumWeeklyConsumeLog(startOfWeek, endOfWeek, memberId);
    }
}
