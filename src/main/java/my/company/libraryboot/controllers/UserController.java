package my.company.libraryboot.controllers;

import lombok.extern.slf4j.Slf4j;
import my.company.libraryboot.model.User;
import my.company.libraryboot.model.enums.Role;
import my.company.libraryboot.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
//@AllArgsConstructor
@Slf4j
public class UserController {

    public static final String REST_URL = "/api/users";
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public Page<User> getAll(@NotNull final Pageable pageable) {
        log.info("getting all users");
        return userService.getAll(pageable);
    }

    @GetMapping(path = "/{id}")
    public Page<User> getUser(@PathVariable int id, @NotNull final Pageable pageable) {
        log.info("getting user {}", id);
        return userService.getUserById(id, pageable);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping(path = "/add-role/{newRole}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addRole(@RequestBody User user, @PathVariable Role newRole) {
        log.info("adding role {} to user {}", newRole, user);
        User result = userService.addRole(user, newRole);
        return ResponseEntity.ok(result);
    }

//    @Secured("ROLE_ADMIN")
//    @GetMapping(path = "/add-role/{id}/{newRole}")
//    public ResponseEntity<User> addRoleTest(@PathVariable int id, @PathVariable Role newRole) {
//        User user = userService.getUserById(id, Pageable.unpaged()).getContent().get(0);
//        log.info("adding role {} to user {}", newRole, id);
//        User result = userService.addRole(user, newRole);
//        return ResponseEntity.ok(result);
//    }

    @Secured("ROLE_ADMIN")
    @PutMapping(path = "/remove-role/{roleToRemove}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> removeRole(@RequestBody User user, @PathVariable Role roleToRemove) {
        log.info("removing role {} from user {}", roleToRemove, user);
        User result = userService.removeRole(user, roleToRemove);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        log.info("deleting user {}", id);
        userService.deleteUser(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody User user) {
        log.info("updating user {}", user);
        userService.updateUser(user);
    }

}
