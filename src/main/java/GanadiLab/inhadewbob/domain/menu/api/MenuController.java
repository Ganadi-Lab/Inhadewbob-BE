package GanadiLab.inhadewbob.domain.menu.api;

import GanadiLab.inhadewbob.domain.menu.dto.MenuDTO;
import GanadiLab.inhadewbob.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/roulette")
    public ResponseEntity<MenuDTO.Response> getMenusByRoulette(
            @RequestParam("date") LocalDate date,
            @RequestParam("category") String category,
            @RequestParam("price") Integer price
    ) {
        return ResponseEntity.ok().body(
                // 임시 회원 번호
                menuService.getMenusByRoulette(date, category, price, 1L)
        );
    }

    @GetMapping("/recom")
    public ResponseEntity<MenuDTO.RecommendResponse> getRecommendPrice(
            @Param("date") LocalDate date
    ) {
        return ResponseEntity.ok().body(
                // 임시 회원 번호
                menuService.getRecommendation(date, 1L)
        );
    }
}
