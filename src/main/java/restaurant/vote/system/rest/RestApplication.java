package restaurant.vote.system.rest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import restaurant.vote.system.rest.entity.Dish;
import restaurant.vote.system.rest.entity.Restaurant;
import restaurant.vote.system.rest.entity.Role;
import restaurant.vote.system.rest.entity.User;
import restaurant.vote.system.rest.repository.DishRepository;
import restaurant.vote.system.rest.repository.RestaurantRepository;
import restaurant.vote.system.rest.repository.UserRepository;

import java.util.*;

@SpringBootApplication
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}

	// Initialize our Repositories with the test data just after Spring-boot started

	@Bean
	CommandLineRunner initUser(UserRepository userRepository) {
		List<String[]> list = new ArrayList<>();
		list.add(new String[]{"admin", "admin", "ROLE_ADMIN"});
		list.add(new String[]{"user", "user", "ROLE_USER"});

		return (args) -> list
				.forEach(
						entry -> userRepository.save(new User(
								entry[0],
								entry[1],
								Role.valueOf(entry[2])
						))
				);
	}

	@Bean
	CommandLineRunner initRestaurant(RestaurantRepository restaurantRepository) {
		return (args) -> Arrays.asList(
				"restaurant1, restaurant2, restaurant3".split(", "))
				.forEach(
						name -> restaurantRepository.save(new Restaurant(name))
				);
	}

	@Bean
	CommandLineRunner initDish(DishRepository dishRepository) {

		Map<String, Integer> dishes = new HashMap<>();

		int priceMin = 50, priceMax = 100 * 100;	// !!! price in cents
		for (int i = 1; i <= 10; i++) {
			dishes.put("dish" + i, (priceMin + (int)(Math.random() * ((priceMax - priceMin) + 1))));
		}

		return (args) -> dishes.forEach(
				(k, v) -> dishRepository.save(new Dish(k, v))
		);
	}
}
