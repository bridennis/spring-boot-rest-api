package restaurant.vote.system.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.vote.system.rest.entity.Dish;

import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findById(Long dishId);
}
