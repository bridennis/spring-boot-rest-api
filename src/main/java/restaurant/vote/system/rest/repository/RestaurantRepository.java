package restaurant.vote.system.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.vote.system.rest.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findById(Long restaurantId);
}
