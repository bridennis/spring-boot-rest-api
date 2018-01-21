package restaurant.vote.system.rest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Dish {

    @JsonIgnore
    @OneToMany(mappedBy = "dish")
    private Set<Menu> menus = new HashSet<>();

    @Id
    @GeneratedValue
    private Long id;

    @Length(min = 1, max = 50)
    private String name;

    /*
        Keep money in cents
    */
    @Min(value = 0, message = "Should be a positive number in cents")
    private Integer price;

    public Dish() {
    }

    public Dish(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public Set<Menu> getMenus() {
        return menus;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }
}
