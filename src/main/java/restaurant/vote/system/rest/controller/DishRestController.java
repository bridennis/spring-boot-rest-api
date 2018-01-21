package restaurant.vote.system.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restaurant.vote.system.rest.entity.Dish;
import restaurant.vote.system.rest.repository.DishRepository;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/dishes")
@PreAuthorize("hasRole('ADMIN')")
public class DishRestController {

    private final DishRepository dishRepository;

    @Autowired
    public DishRestController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Dish> readAll() {
        return this.dishRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{dishId}")
    public Dish readOne(@Valid @PathVariable Long dishId) {
        return this.dishRepository.findById(dishId).orElseThrow(ResourceNotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Dish add(@Valid @RequestBody Dish newDish) {
        return dishRepository.save(new Dish(newDish.getName(), newDish.getPrice()));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{dishId}")
    public Dish update(@Valid @PathVariable Long dishId, @Valid @RequestBody Dish newDish) {

        return this.dishRepository
                .findById(dishId)
                .map(dish -> {
                    dish.setName(newDish.getName());
                    dish.setPrice(newDish.getPrice());
                    dishRepository.save(dish);
                    return dish;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{dishId}")
    public ResponseEntity<?> del(@Valid @PathVariable Long dishId) {

        return this.dishRepository
                .findById(dishId)
                .map(restaurant -> {
                    dishRepository.delete(restaurant);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> dishesDel() {
        dishRepository.deleteAll();
        return ResponseEntity.ok().build();
    }

}
