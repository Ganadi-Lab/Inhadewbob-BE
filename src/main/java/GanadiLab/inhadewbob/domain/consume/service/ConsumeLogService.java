package GanadiLab.inhadewbob.domain.consume.service;

import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogCreateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogUpdateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeLogResponse;
import GanadiLab.inhadewbob.domain.consume.model.ConsumeLog;
import GanadiLab.inhadewbob.domain.consume.repository.ConsumeLogRepository;
import GanadiLab.inhadewbob.domain.member.model.Member;
import GanadiLab.inhadewbob.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                        .spentAmount(req.getSpentAmount())
                        .remainingBudget(req.getRemainingBudget())
                        .date(req.getDate())
                        .build()
        );

        return toResponse(saved);
    }

    // 수정
    public ConsumeLogResponse update(Long id, ConsumeLogUpdateRequest req) {

        ConsumeLog log = consumeLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("소비 기록이 존재하지 않습니다."));

        log = ConsumeLog.builder()
                .id(log.getId())
                .member(log.getMember())
                .date(log.getDate())
                .spentAmount(req.getSpentAmount())
                .remainingBudget(req.getRemainingBudget())
                .build();

        consumeLogRepository.save(log);

        return toResponse(log);
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
