package GanadiLab.inhadewbob.domain.menu.repository;

import GanadiLab.inhadewbob.domain.menu.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    /* 전 주에 먹은 음식 제외, 카테고리/가격 상한 내에서 랜덤 추천 -> 4개 */
    @Query(value = "SELECT * FROM menu " +
            "WHERE category IN (:categories) " +
            "AND price <= :price " +
            "AND menu_id NOT IN (:lastWeeks) " +
            "ORDER BY RAND() LIMIT 4", nativeQuery = true)
    List<Menu> findMenusByRoulette(@Param("categories") List<String> categories,
                                   @Param("price") Integer price,
                                   @Param("lastWeeks") List<Long> lastWeeks);

    /* +- 500원 메뉴 추천 -> 2개(앞서 추천한 4개 식당은 제외) */
    @Query(value = "SELECT * FROM menu " +
            "WHERE category IN (:categories) " +
            "AND price = :price " +
            "AND menu_id NOT IN (:lastWeeks) " +
            "AND menu_id NOT IN (:selectedMenus)" +
            "ORDER BY RAND() LIMIT 2", nativeQuery = true)
    List<Menu> findMenusAround(@Param("categories") List<String> categories,
                                 @Param("price") Integer price,
                                 @Param("lastWeeks") List<Long> lastWeeks,
                                 @Param("selectedMenus") List<Long> selectedMenus);
}
