package my.company.libraryboot.controllers;

import my.company.libraryboot.error.EntityNotFoundException;
import my.company.libraryboot.model.Author;
import my.company.libraryboot.model.enums.Gender;
import my.company.libraryboot.repository.AuthorRepository;
import my.company.libraryboot.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;

import static my.company.libraryboot.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AuthorController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorController {

        public static final String REST_URL = "/api/authors";
        // TODO: why 'field injection' is not recommended?
        @Autowired
        AuthorRepository authorRepository;
        @Autowired
        AuthorService authorService;

        @GetMapping()
        public ResponseEntity<Page<Author>> getAll(@NotNull final Pageable pageable) {
                Page<Author> result = authorRepository.findAll(pageable);
                return ResponseEntity.ok(result);
        }

        // http://localhost:8080/api/auhtors/sorted?pageNo=0&pageSize=10&sortBy=coutry
        // TODO: add ascending or descending order
        @GetMapping(path = "/sorted")
        public Page<Author> getAllSortedByParam(
                @RequestParam(defaultValue = "0") Integer pageNo,
                @RequestParam(defaultValue = "20") Integer pageSize,
                @RequestParam(defaultValue = "title") String sortBy)
        {
                return authorService.getAllSorted(pageNo, pageSize, sortBy);
        }

        @GetMapping(path = "/by-name/{name}")
        public Page<Author> getBooksByAuthor(@PathVariable String name, @NotNull final Pageable pageable) {
                return authorService.getBooksByAuthorName(name, pageable);
        }

        @GetMapping(path = "/{id}")
        public Page<Author> getById(@PathVariable int id, @NotNull final Pageable pageable) {
                Page<Author> result = authorRepository.findAuthorById(id, pageable);
                if (!result.isEmpty())
                   return result;
                else
                   throw new EntityNotFoundException(String.format("Author with id %d doesn't exist!", id));
        }

        @GetMapping(path = "/by-country/{country}")
        public Page<Author> getByCountry(@PathVariable String country, @NotNull final Pageable pageable) {
                return authorRepository.findAuthorsByCountry(country.toUpperCase(), pageable);
        }

        @GetMapping(path = "/by-gender/{gender}")
        public Page<Author> getByGender(@PathVariable Gender gender, @NotNull final Pageable pageable) {
                return authorRepository.findAuthorsByGender(gender, pageable);
        }

        /**
         *  ============================NON-IDEMPOTENT=====================================
         */
        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<Author> addNewAuthor(@RequestBody Author newAuthor) {
                checkNew(newAuthor);
                Author created = authorRepository.save(newAuthor);
                URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path(REST_URL + "/{id}")
                        .buildAndExpand(created.getId()).toUri();
                return ResponseEntity.created(uriOfNewResource).body(created);
        }

        @DeleteMapping(path = "/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteAuthor(@PathVariable int id) {
                authorRepository.deleteAuthorById(id);
        }

        @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void updateAuthor(@RequestBody Author author) {
                authorRepository.save(author);
        }
}
