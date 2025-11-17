package GanadiLab.inhadewbob.domain.menu.repository;

import GanadiLab.inhadewbob.domain.menu.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
