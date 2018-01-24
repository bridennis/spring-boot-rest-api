package restaurant.vote.system.rest.service;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import restaurant.vote.system.rest.entity.*;
import restaurant.vote.system.rest.repository.*;

import java.security.Principal;
import java.time.LocalTime;

/*
    Бизнес логика для RestaurantRestController,
 */
@Service
public class RestaurantService {

    // permit vote only before 11:00
    private static final int VOTE_HOUR_BEFORE = 11;
    private static final int VOTE_MINUTE_BEFORE = 0;

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final VoteResultRepository voteResultRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MenuRepository menuRepository, VoteRepository voteRepository, UserRepository userRepository, VoteResultRepository voteResultRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.voteResultRepository = voteResultRepository;
    }

    public Vote vote(Long restaurantId, Principal principal) {

        Vote vote = null;

        if (LocalTime.now().isBefore(LocalTime.of(VOTE_HOUR_BEFORE, VOTE_MINUTE_BEFORE))) {

            User user = userRepository.findByLogin(principal.getName()).orElse(null);

            if (user != null) {

                // restaurant MUST exists and MUST be in menu

                Restaurant restaurant = restaurantRepository.findOne(restaurantId);
                if (restaurant != null) {
                    restaurant = menuRepository.findByRestaurant(restaurant).map(Menu::getRestaurant).orElse(null);
                }

                if (restaurant == null) {
                    throw new ResourceNotFoundException();
                } else {
                    vote = voteRepository.findByUser(user).orElse(null);

                    if (vote != null) {

                        // Пользователь уже голосовал

                        Restaurant legacyRestaurant = vote.getRestaurant();

                        if (legacyRestaurant != restaurant) {

                            vote.setRestaurant(restaurant);

                            // Обновляем счетчик голосования (уменьшаем в предыдущем, увеличиваем в новом)

                            VoteResult voteResult = voteResultRepository.findByRestaurant(legacyRestaurant).orElse(null);

                            if (voteResult != null) {
                                voteResult.setCounter(voteResult.getCounter() > 0 ? voteResult.getCounter() - 1 : 0);
                                voteResultRepository.save(voteResult);
                            }

                            voteResult = voteResultRepository.findByRestaurant(restaurant).orElse(null);

                            if (voteResult == null) {
                                voteResult = new VoteResult(restaurant, 1L);
                            } else {
                                voteResult.setCounter(voteResult.getCounter() + 1);
                            }
                            voteResultRepository.save(voteResult);

                        }

                    } else {

                        // Проголосовал новый пользователь

                        vote = new Vote(user, restaurant);

                        // Обновляем счетчик голосования

                        VoteResult voteResult = voteResultRepository.findByRestaurant(restaurant).orElse(null);

                        if (voteResult == null) {
                            voteResult = new VoteResult(restaurant, 1L);
                        } else {
                            voteResult.setCounter(voteResult.getCounter() + 1);
                        }
                        voteResultRepository.save(voteResult);
                    }
                    voteRepository.save(vote);

                }
            }
        }

        return vote;
    }
}
