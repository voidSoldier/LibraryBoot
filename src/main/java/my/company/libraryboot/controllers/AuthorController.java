package my.company.libraryboot.controllers;

import my.company.libraryboot.model.Author;
import my.company.libraryboot.repository.AuthorRepository;
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
@RequestMapping(value = AuthorController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorController {

        public static final String REST_URL = "/api/authors";
        @Autowired
        AuthorRepository authorRepository;

        @GetMapping()
        public ResponseEntity<Page<Author>> getAll(@NotNull final Pageable pageable) {
        Page<Author> result = authorRepository.findAll(pageable);
        return ResponseEntity.ok(result);
        }
}
