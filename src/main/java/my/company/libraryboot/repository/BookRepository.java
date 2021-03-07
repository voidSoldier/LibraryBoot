package my.company.libraryboot.repository;

import my.company.libraryboot.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Integer> {

    @RestResource(rel = "by-title", path = "by-title")
    Page<Book> findBookByTitle(String title, Pageable page);
}
