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

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;

@Service
@RequiredArgsConstructor
public class ConsumeLogService {

    private final ConsumeLogRepository consumeLogRepository;
    private final MemberRepository memberRepository;

    // 등록
    public ConsumeLogResponse create(ConsumeLogCreateRequest req) {

        Member member = memberRepository.findById(req.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        ConsumeLog saved = consumeLogRepository.save(
                ConsumeLog.builder()
                        .member(member)
                        .remainingBudget(req.getRemainingBudget())
                        .date(req.getDate())
                        .build()
        );

        return toResponse(saved);
    }

    public ConsumeLogResponse update(Long id, ConsumeLogUpdateRequest req) {

        ConsumeLog log = consumeLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("소비 기록이 존재하지 않습니다."));

        // null 체크 후 값만 업데이트
        if (req.getSpentAmount() != null) {
            log.setSpentAmount(req.getSpentAmount());
        }
        if (req.getRemainingBudget() != null) {
            log.setRemainingBudget(req.getRemainingBudget());
        }

        consumeLogRepository.save(log);

        return toResponse(log);
    }

    public ConsumeStatusResponse getConsumeStatus(Long memberId, LocalDate today) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        Integer budget = member.getWeeklyBudget();

        // 지난주
        LocalDate lastWeekDay = today.minusWeeks(1);

        Integer thisWeekSpent = sumWeeklySpent(today);
        Integer lastWeekSpent = sumWeeklySpent(lastWeekDay);

        return ConsumeStatusResponse.builder()
                .budget(budget)
                .thisWeekSpent(thisWeekSpent)
                .differenceFromLastWeekSpent(lastWeekSpent - thisWeekSpent)
                .build();
    }

    // 이번주, 지난주, 지지난주 소비 통계
    public ConsumeStatResponse getConsumeStats(LocalDate today) {

        // 현재 달
        int currentMonth = today.getMonthValue();
        // 이번 달의 몇주차인지
        int currentWeek = today.get(ChronoField.ALIGNED_WEEK_OF_MONTH);

        // 지난주
        LocalDate lastWeekDay = today.minusWeeks(1);
        // 지지난주
        LocalDate twoWeeksAgoDay = lastWeekDay.minusWeeks(1);

        Integer thisWeekSpent = sumWeeklySpent(today);
        Integer lastWeekSpent = sumWeeklySpent(lastWeekDay);
        Integer twoWeekSpent = sumWeeklySpent(twoWeeksAgoDay);

        return ConsumeStatResponse.builder()
                .currentMonth(currentMonth)
                .currentWeek(currentWeek)
                .thisWeekSpent(thisWeekSpent)
                .lastWeekSpent(lastWeekSpent)
                .twoWeeksAgoSpent(twoWeekSpent)
                .build();
    }

    private Integer sumWeeklySpent(LocalDate date) {
        
        // 주차 날짜 범위 계산
        String startOfWeek = DateUtil.getStartOfWeek(date).toString();
        String endOfWeek = DateUtil.getEndOfWeek(date).toString();

        return consumeLogRepository.sumWeeklyConsumeLog(startOfWeek, endOfWeek);
    }

    // DTO 변환
    private ConsumeLogResponse toResponse(ConsumeLog log) {
        return ConsumeLogResponse.builder()
                .id(log.getId())
                .memberId(log.getMember().getId())
                .spentAmount(log.getSpentAmount())
                .remainingBudget(log.getRemainingBudget())
                .date(log.getDate())
                .createdAt(log.getCreatedAt().toString())
                .build();
    }
}
