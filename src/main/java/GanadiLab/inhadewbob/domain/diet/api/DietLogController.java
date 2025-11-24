package GanadiLab.inhadewbob.domain.diet.api;

import GanadiLab.inhadewbob.domain.diet.dto.request.DietLogCreateRequest;
import GanadiLab.inhadewbob.domain.diet.dto.response.DietLogResponse;
import GanadiLab.inhadewbob.domain.diet.dto.response.LatestDietLogResponse;
import GanadiLab.inhadewbob.domain.diet.dto.response.WeeklyDietLogResponse;
import GanadiLab.inhadewbob.domain.diet.service.DietLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/diets")
@RequiredArgsConstructor
public class DietLogController {
    private final DietLogService dietLogService;

    @PostMapping
    public DietLogResponse create(@RequestBody DietLogCreateRequest request) {

        return dietLogService.create(request);
    }

    @GetMapping("/{memberId}")
    public List<DietLogResponse> getByMember(@PathVariable Long memberId) {

        return dietLogService.getByMember(memberId);
    }

    @GetMapping("/{memberId}/daily")
    public List<DietLogResponse> getDaily(
            @PathVariable Long memberId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return dietLogService.getDaily(memberId, date);
    }

    @GetMapping("/weekly")
    public List<WeeklyDietLogResponse> getWeekly(
            @RequestParam("start") LocalDate start,
            @RequestParam("end") LocalDate end
    ) {
        // 임시 회원 번호
        return dietLogService.getWeekly(start, end, 1L);
    }

    @GetMapping("/latest")
    public List<LatestDietLogResponse> getLatest() {
        // 임시 회원 번호
        return dietLogService.getLatest(1L);
    }

}