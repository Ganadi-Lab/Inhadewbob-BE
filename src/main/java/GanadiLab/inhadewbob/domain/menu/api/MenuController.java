package GanadiLab.inhadewbob.domain.menu.api;

import GanadiLab.inhadewbob.domain.menu.dto.MenuDTO;
import GanadiLab.inhadewbob.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
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


    @GetMapping
    public ResponseEntity<MenuDTO.Response> getMenusByRoulette(
            @RequestParam("date") LocalDate date,
            @RequestParam("category") String category,
            @RequestParam("price") Integer price
    ) {
        return ResponseEntity.ok().body(
                menuService.getMenusByRoulette(date, category, price)
        );
    }
}
