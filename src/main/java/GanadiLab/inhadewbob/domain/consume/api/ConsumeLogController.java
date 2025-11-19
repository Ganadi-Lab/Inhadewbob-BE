package GanadiLab.inhadewbob.domain.consume.api;

import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogCreateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.request.ConsumeLogUpdateRequest;
import GanadiLab.inhadewbob.domain.consume.dto.response.ConsumeLogResponse;
import GanadiLab.inhadewbob.domain.consume.service.ConsumeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/consumes")
public class ConsumeLogController {

    private final ConsumeLogService consumeLogService;

    // 소비 현황 등록
    @PostMapping
    public ConsumeLogResponse create(@RequestBody ConsumeLogCreateRequest req) {
        return consumeLogService.create(req);
    }

    // 소비 현황 수정
    @PutMapping("/{id}")
    public ConsumeLogResponse update(
            @PathVariable Long id,
            @RequestBody ConsumeLogUpdateRequest req) {

        return consumeLogService.update(id, req);
    }
}