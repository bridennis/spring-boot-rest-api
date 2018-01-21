package restaurant.vote.system.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.vote.system.rest.entity.Menu;
import restaurant.vote.system.rest.entity.Restaurant;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findById(Long menuId);
    Optional<Menu> findByRestaurant(Restaurant restaurant);
}
