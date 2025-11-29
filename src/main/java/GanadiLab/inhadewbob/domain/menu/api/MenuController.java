package GanadiLab.inhadewbob.domain.menu.api;

import GanadiLab.inhadewbob.domain.menu.dto.MenuDTO;
import GanadiLab.inhadewbob.domain.menu.service.MenuService;
import GanadiLab.inhadewbob.global.security.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    /* 랜덤 룰렛 돌리기 */
    @GetMapping("/roulette")
    public ResponseEntity<MenuDTO.Response> getMenusByRoulette(
            @RequestParam("date") LocalDate date,
            @RequestParam("category") List<String> categories,
            @RequestParam("price") Integer price,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return ResponseEntity.ok().body(
                menuService.getMenusByRoulette(date, categories, price, principal.getMember())
        );
    }

    /* 추천 예산 조회 */
    @GetMapping("/recom")
    public ResponseEntity<MenuDTO.RecommendResponse> getRecommendPrice(
            @Param("date") LocalDate date,
            @AuthenticationPrincipal PrincipalDetails principal
    ) {
        return ResponseEntity.ok().body(
                menuService.getRecommendation(date, principal.getMember())
        );
    }
}
