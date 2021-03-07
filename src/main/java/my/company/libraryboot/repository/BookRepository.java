package my.company.libraryboot.repository;

import my.company.libraryboot.model.Book;
import my.company.libraryboot.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Integer> {

    @RestResource(rel = "by-title", path = "by-title")
    Page<Book> findBookByTitle(String title, Pageable page);

//    @RestResource(rel = "by-genres", path = "by-genres")
//    Page<Book> findBooksByGenres(String genres, Pageable page);

    // http://localhost:8080/api/books/search/by-title?title=Moby%20Dick
    // http://localhost:8080/api/books/search/by-genres?genres=FANTASY
}
