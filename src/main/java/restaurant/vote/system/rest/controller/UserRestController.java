/**
 * User REST controller
 */

package restaurant.vote.system.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import restaurant.vote.system.rest.entity.Role;
import restaurant.vote.system.rest.entity.User;
import restaurant.vote.system.rest.repository.UserRepository;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserRepository userRepository;

    @Autowired
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public Collection<User> readAll() {
        return this.userRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public User readOne(@Valid @PathVariable Long userId) {
        return this.userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/reg")
    public User reg(@Valid @RequestBody User user) {
        return userRepository.save(new User(user.getLogin(), user.getPassword(), Role.ROLE_USER));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public User add(@Valid @RequestBody User user) {
        return userRepository.save(new User(user.getLogin(), user.getPassword(), effectiveRole(user)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, value = "/{userId}")
    public User update(@Valid @PathVariable Long userId, @Valid @RequestBody User user) {

        return this.userRepository
                .findById(userId)
                .map(newUser -> {
                    newUser.setLogin(user.getLogin());
                    newUser.setPassword(user.getPassword());
                    newUser.setRole(effectiveRole(user));
                    userRepository.save(user);
                    return newUser;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    public ResponseEntity<?> del(@Valid @PathVariable Long userId) {

        return this.userRepository
                .findById(userId)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /*
        Get effective role

        The ROLE_USER is a default role
        Only user with the role ROLE_ADMIN has permit to change user's role
   */
    private Role effectiveRole(User user) {

        boolean hasAdminRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        Role role = Role.ROLE_USER;
        if (user.getRole() != null && hasAdminRole) {
            role = user.getRole();
        }

        return role;
    }

}