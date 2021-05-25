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

@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, Integer> {

    /*
     *  @RestResource is ADMIN access only!
     *  @RestResource(rel = "by-title", path = "by-title")
     *  @RestResource(rel = "by-author", path = "by-author")
     *  @RestResource(rel = "by-genre", path = "by-genre")
     *
     *      http://localhost:8080/api/books/search/by-title?title=Moby%20Dick
     *      http://localhost:8080/api/books/search/by-genre?genre=FANTASY
     *      http://localhost:8080/api/books/search/by-author?author=Abercrombie
     */
//
    Page<Book> findBookByTitle(String title, Pageable page);

//    @Transactional
//    @EntityGraph(attributePaths = {"coverImage"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query("SELECT b FROM Book b LEFT JOIN b.coverImage ci WHERE b.id = :id")
//    Page<Book> findBookWithCover(int id, Pageable page);

//    @Query("SELECT b FROM Book b LEFT JOIN b.coverImage ci WHERE b.id =:id")
//    Book findBookWithCover(int id);

    // TODO: is EntityGraph necessary since it's used to fix N+1 problem which should not appear due to Session Open-in-View is set to false?
    @EntityGraph(attributePaths = {"authors"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b LEFT JOIN b.authors a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT(:name, '%')) OR LOWER(a.lastName) LIKE LOWER(CONCAT(:name, '%'))")
    Page<Book> getBooksByAuthorName(@Param("name") String name, Pageable page);
    // TODO: is EntityGraph necessary?
    @EntityGraph(attributePaths = {"authors"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b LEFT JOIN b.authors a WHERE LOWER(CONCAT(a.firstName, ' ', a.lastName)) LIKE LOWER(CONCAT(:name, '%'))")
    Page<Book> getBooksByAuthorFullName(@Param("name") String name, Pageable page);


//    @EntityGraph(attributePaths = {"genres"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query("SELECT b FROM Book b JOIN FETCH b.genres g WHERE g")
    Page<Book> findBooksByGenresContaining(Genre genre, Pageable page);

    Page<Book> findBooksByBookType(BookType bookType, Pageable pageable);

    Page<Book> findBooksByOwned(boolean owned, Pageable pageable);

    Page<Book> findBooksByFinished(boolean finished, Pageable pageable);

    Page<Book> findBooksByLovedTrue(Pageable pageable);

    // TODO: is 'genres' necessary?
    @EntityGraph(attributePaths = {"authors", "genres"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Page<Book> findBookById(int id, Pageable page);

    @Transactional
    @Modifying
    @Query("DELETE FROM Book b WHERE b.id = :id")
    int deleteBookById(int id);

}
