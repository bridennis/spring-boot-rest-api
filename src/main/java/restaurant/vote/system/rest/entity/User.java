package restaurant.vote.system.rest.entity;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    @Length(min = 1, max = 32)
    @Column(unique=true)
    private String login;

    @NotBlank
    @JsonProperty(access = WRITE_ONLY)
    @Length(min = 4, max = 32)
    private String password;

    @JsonProperty(access = WRITE_ONLY)
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.role = Role.ROLE_USER;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
