package my.company.libraryboot.controllers;

import my.company.libraryboot.model.User;
import my.company.libraryboot.model.enums.Role;
import my.company.libraryboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = UserController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    public static final String REST_URL = "/api/users";
    @Autowired
    UserRepository userRepository;

    @GetMapping()
    public ResponseEntity<Page<User>> getAll(@NotNull final Pageable pageable) {
        Page<User> result = userRepository.findAll(pageable);
        return ResponseEntity.ok(result);
    }

    // TODO: test this
    // TODO: is this needed? ---> <global-method-security secured-annotations="enabled" />
    // use userId instead of RequestBody?
    @Secured("ROLE_ADMIN")
    @PutMapping(path = "/add-role/{newRole}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addRole(@RequestBody User user, @PathVariable Role newRole) {
        Set<Role> rolesUpdated = new HashSet<>(user.getRoles());
        rolesUpdated.add(newRole);
        user.setRoles(rolesUpdated);
        userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    // TODO: add CRUD methods
}
