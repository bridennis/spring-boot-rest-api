package restaurant.vote.system.rest.entity;

import javax.persistence.*;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Dish dish;

    @ManyToOne
    private Restaurant restaurant;

    public Menu() {
    }

    public Menu(Dish dish, Restaurant restaurant) {
        this.dish = dish;
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
