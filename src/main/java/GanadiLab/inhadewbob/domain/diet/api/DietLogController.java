package GanadiLab.inhadewbob.domain.diet.api;

import GanadiLab.inhadewbob.domain.diet.dto.request.DietLogCreateRequest;
import GanadiLab.inhadewbob.domain.diet.dto.response.DietLogResponse;
import GanadiLab.inhadewbob.domain.diet.dto.response.LatestDietLogResponse;
import GanadiLab.inhadewbob.domain.diet.dto.response.WeeklyDietLogResponse;
import GanadiLab.inhadewbob.domain.diet.service.DietLogService;
import GanadiLab.inhadewbob.global.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.time.LocalDate;

@RestController
@RequestMapping("/diets")
@RequiredArgsConstructor
public class DietLogController {
    private final DietLogService dietLogService;

    @PostMapping
    public DietLogResponse create(@AuthenticationPrincipal PrincipalDetails principal, @RequestBody DietLogCreateRequest request) {
        Long memberId = principal.getMember().getId();
        return dietLogService.create(memberId, request);
    }

    @GetMapping
    public List<DietLogResponse> getByMember(@AuthenticationPrincipal PrincipalDetails principal) {
        Long memberId = principal.getMember().getId();
        return dietLogService.getByMember(memberId);
    }

    @GetMapping("/daily")
    public List<DietLogResponse> getDaily(
            @AuthenticationPrincipal PrincipalDetails principal,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        Long memberId = principal.getMember().getId();
        return dietLogService.getDaily(memberId, date);
    }

    /* 주별 식단 기록 조회 */
    @GetMapping("/weekly")
    public List<WeeklyDietLogResponse> getWeekly(
            @RequestParam("start") LocalDate start,
            @RequestParam("end") LocalDate end,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return dietLogService.getWeekly(start, end, principal.getMember());
    }

    /* 최근 식단 기록 조회 (5개) */
    @GetMapping("/latest")
    public List<LatestDietLogResponse> getLatest(
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return dietLogService.getLatest(principal.getMember());
    }

    @DeleteMapping("/{dietLogId}")
    public void delete(
            @AuthenticationPrincipal PrincipalDetails principal,
            @PathVariable Long dietLogId
    ) {
        dietLogService.delete(dietLogId, principal.getMember().getId());
    }

}