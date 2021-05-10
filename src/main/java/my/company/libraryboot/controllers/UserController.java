package my.company.libraryboot.controllers;

import my.company.libraryboot.model.User;
import my.company.libraryboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.NotNull;

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
}
