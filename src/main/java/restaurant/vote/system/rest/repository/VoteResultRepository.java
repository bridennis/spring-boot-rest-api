package restaurant.vote.system.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import restaurant.vote.system.rest.entity.Restaurant;
import restaurant.vote.system.rest.entity.VoteResult;

import java.util.Optional;

public interface VoteResultRepository extends JpaRepository<VoteResult, Long> {
    Optional<VoteResult> findByRestaurant(Restaurant restaurant);
}
