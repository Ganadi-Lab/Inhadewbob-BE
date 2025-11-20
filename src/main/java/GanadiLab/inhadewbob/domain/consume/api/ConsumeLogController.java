package GanadiLab.inhadewbob.domain.consume.api;

import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogCreateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogUpdateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeLogResponse;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeStatResponse;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeStatusResponse;
import GanadiLab.inhadewbob.domain.consume.service.ConsumeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumes")
public class ConsumeLogController {

    private final ConsumeLogService consumeLogService;

    // 소비 현황 등록
    @PostMapping
    public ConsumeLogResponse create(@RequestBody ConsumeLogCreateRequest req) {
        return consumeLogService.createConsumeLog(req);
    }

    // 소비 현황 수정
    @PatchMapping("/{id}")
    public ConsumeLogResponse update(
            @PathVariable Long id,
            @RequestBody ConsumeLogUpdateRequest req) {

        return consumeLogService.updateConsumeLog(id, req);
    }

    /* 소비 현황 조회 */
    @GetMapping("/status")
    public ResponseEntity<ConsumeStatusResponse> getConsumeStat(
            @RequestParam("date") LocalDate date
    ) {
        return ResponseEntity.ok().body(
                // 아직 임의의 memberId 삽입 -> 추후 로그인한 회원으로 변경
                consumeLogService.getConsumeStatus(1L, date)
        );
    }

    /* 소비 통계 조회 */
    @GetMapping("/statistics")
    public ResponseEntity<ConsumeStatResponse> getConsumeStats(
            @RequestParam("date") LocalDate date
    ) {
        return ResponseEntity.ok().body(
                consumeLogService.getConsumeStats(date)
        );
    }
}