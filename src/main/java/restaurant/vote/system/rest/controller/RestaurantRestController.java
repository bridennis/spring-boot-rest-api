/*
 * Restaurant REST controller
 */

package restaurant.vote.system.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restaurant.vote.system.rest.entity.*;
import restaurant.vote.system.rest.repository.*;
import restaurant.vote.system.rest.service.RestaurantService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalTime;
import java.util.Collection;

@RestController
@RequestMapping("/restaurants")
public class RestaurantRestController {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final VoteRepository voteRepository;
    private final DishRepository dishRepository;
    private final VoteResultRepository voteResultRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantRestController(
            RestaurantRepository restaurantRepository,
            MenuRepository menuRepository,
            VoteRepository voteRepository,
            DishRepository dishRepository,
            VoteResultRepository voteResultRepository,
            RestaurantService restaurantService
    ) {
        this.restaurantRepository = restaurantRepository;
        this.menuRepository = menuRepository;
        this.voteRepository = voteRepository;
        this.dishRepository = dishRepository;
        this.voteResultRepository = voteResultRepository;
        this.restaurantService = restaurantService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public Collection<Restaurant> readAll() {
        return restaurantRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/{restaurantId}")
    public Restaurant readOne(@Valid @PathVariable Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(ResourceNotFoundException::new);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public Restaurant add(@Valid @RequestBody Restaurant newRestaurant) {
        return restaurantRepository.save(new Restaurant(newRestaurant.getName()));
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST, value = "/{restaurantId}/vote")
    public Vote updateVote(@Valid @PathVariable Long restaurantId, Principal principal) {
        return restaurantService.vote(restaurantId, principal);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST, value = "/{restaurantId}/dish/{dishId}")
    public Collection<Menu> addDish(@Valid @PathVariable Long restaurantId, @Valid @PathVariable Long dishId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(ResourceNotFoundException::new);
        Dish dish = dishRepository.findById(dishId).orElseThrow(ResourceNotFoundException::new);

        menuRepository.save(new Menu(dish, restaurant));

        return menuRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, value = "/{restaurantId}")
    public Restaurant update(@Valid @PathVariable Long restaurantId, @Valid @RequestBody Restaurant newRestaurant) {
        return restaurantRepository
                .findById(restaurantId)
                .map(restaurant -> {
                    restaurant.setName(newRestaurant.getName());
                    restaurantRepository.save(restaurant);
                    return restaurant;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{restaurantId}")
    public ResponseEntity<?> del(@Valid @PathVariable Long restaurantId) {

        return restaurantRepository
                .findById(restaurantId)
                .map(restaurant -> {
                    restaurantRepository.delete(restaurant);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE, value = "/menus/all")
    public ResponseEntity<?> menusDel() {
        menuRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/menus")
    public Collection<Menu> menus() {
        return menuRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE, value = "/votes")
    public ResponseEntity<?> votesDel() {
        voteRepository.deleteAll();
        voteResultRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/votes")
    public Collection<Vote> votes() {
        return voteRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/votes/results")
    public Collection<VoteResult> votesResult() {
        return  voteResultRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{restaurantId}/dish/{dishId}")
    public ResponseEntity<?> menusItemDel(@Valid @PathVariable Long restaurantId, @Valid @PathVariable Long dishId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        Dish dish = dishRepository.findById(dishId).orElse(null);
        if (restaurant != null && dish != null) {
            return menuRepository
                    .findByRestaurantAndDish(restaurant, dish)
                    .map(menu -> {
                        menuRepository.delete(menu);
                        return ResponseEntity.ok().build();
                    })
                    .orElseThrow(ResourceNotFoundException::new);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> restaurantsDel() {
        restaurantRepository.deleteAll();
        return ResponseEntity.ok().build();
    }


}
