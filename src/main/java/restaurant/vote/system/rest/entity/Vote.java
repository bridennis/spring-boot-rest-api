package restaurant.vote.system.rest.entity;

import javax.persistence.*;

@Entity
public class Vote {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    @ManyToOne
    private Restaurant restaurant;

    public Vote() {
    }

    public Vote(User user, Restaurant restaurant) {
        this.user = user;
        this.restaurant = restaurant;
    }

    public User getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
