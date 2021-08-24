package my.company.libraryboot.repository;

import my.company.libraryboot.model.Author;
import my.company.libraryboot.model.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @EntityGraph(attributePaths = {"books"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT a FROM Author a")
    Page<Author> getAll(Pageable pageable);

//    @RestResource(rel = "by-country", path = "by-country")
    @EntityGraph(attributePaths = {"books"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Author> findAuthorsByCountry(String country, Pageable page);

    @EntityGraph(attributePaths = {"books"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Author> findAuthorsByGender(Gender gender, Pageable page);

    @EntityGraph(attributePaths = {"books"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT a FROM Author a WHERE a.id = :id")
    Page<Author> findAuthorById(int id, Pageable page);

    @Modifying
    @Query("DELETE FROM Author a WHERE a.id = :id")
    int deleteAuthorById(int id);

    // http://localhost:8080/api/authors/search/by-name?name=
    // http://localhost:8080/api/authors/search/by-country?country=
}
