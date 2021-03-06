package my.company.libraryboot.repository;

import my.company.libraryboot.model.Book;
import my.company.libraryboot.model.enums.BookType;
import my.company.libraryboot.model.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Integer> {

    // https://tech.asimio.net/2020/11/06/Preventing-N-plus-1-select-problem-using-Spring-Data-JPA-EntityGraph.html
    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b")
    Page<Book> getAll(Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b")
    List<Book> getAll();

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT(:title, '%'))")
    Page<Book> findBookByTitle(String title, Pageable page);

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b LEFT JOIN b.authors a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT(:name, '%')) OR LOWER(a.lastName) LIKE LOWER(CONCAT(:name, '%'))")
    Page<Book> getBooksByAuthorName(@Param("name") String name, Pageable page);

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b LEFT JOIN b.authors a WHERE LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE LOWER(CONCAT(:name, '%'))")
    Page<Book> getBooksByAuthorFullName(@Param("name") String name, Pageable page);

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Book> findBooksByGenresContaining(Genre genre, Pageable page);

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Book> findBooksByBookType(BookType bookType, Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Book> findBooksByOwned(boolean owned, Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Book> findBooksByFinished(boolean finished, Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Book> findBooksByLovedTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Page<Book> findBookById(int id, Pageable page);

    @Transactional
    @Modifying
    @Query("DELETE FROM Book b WHERE b.id = :id")
    int deleteBookById(int id);

}
