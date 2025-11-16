package GanadiLab.inhadewbob.domain.restaurant.repository;

import GanadiLab.inhadewbob.domain.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
