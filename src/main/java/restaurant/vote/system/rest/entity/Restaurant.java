package restaurant.vote.system.rest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Restaurant {

    @JsonIgnore
    @OneToMany(mappedBy = "restaurant")
    private Set<Menu> menus = new HashSet<>();

    @Id
    @GeneratedValue
    private Long id;

    @Length(min = 1, max = 50)
    private String name;

    public Restaurant() {
    }

    public Restaurant(String name) {
        this.name = name;
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

    public void setName(String name) {
        this.name = name;
    }
}
