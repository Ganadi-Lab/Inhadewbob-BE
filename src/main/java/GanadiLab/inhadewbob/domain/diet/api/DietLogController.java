package GanadiLab.inhadewbob.domain.diet.api;

import GanadiLab.inhadewbob.domain.diet.dto.request.DietLogCreateRequest;
import GanadiLab.inhadewbob.domain.diet.dto.response.DietLogResponse;
import GanadiLab.inhadewbob.domain.diet.service.DietLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}