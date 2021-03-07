package my.company.libraryboot.repository;

import my.company.libraryboot.model.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @RestResource(rel = "by-name", path = "by-name")
    Page<Author> findAuthorByNameContaining(String name, Pageable page);

    @RestResource(rel = "by-country", path = "by-country")
    Page<Author> findAuthorsByCountry(String country, Pageable page);

    // http://localhost:8080/api/authors/search/by-name?name=
    // http://localhost:8080/api/authors/search/by-country?country=

}
