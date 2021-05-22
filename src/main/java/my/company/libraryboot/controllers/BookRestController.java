package my.company.libraryboot.controllers;

import my.company.libraryboot.error.EntityNotFoundException;
import my.company.libraryboot.model.Book;
import my.company.libraryboot.model.enums.BookType;
import my.company.libraryboot.model.enums.Genre;
import my.company.libraryboot.repository.BookRepository;
import my.company.libraryboot.service.BookService;
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
@RequestMapping(value = BookRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class BookRestController {

    public static final String REST_URL = "/api/books";
//    @Autowired
    BookRepository bookRepository;
//    @Autowired
    BookService bookService;

    public BookRestController(BookRepository bookRepository,  BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @GetMapping()
    public Page<Book> getAll(@NotNull final Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    // http://localhost:8080/api/books/sorted?pageNo=0&pageSize=10&sortBy=finished
    // TODO: add ascending or descending order
    @GetMapping(path = "/sorted")
    public Page<Book> getAllSortedByParam(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "title") String sortBy)
    {
        return bookService.getAllSorted(pageNo, pageSize, sortBy);
    }

//    @GetMapping(path = "/filter-by-author/{authorName}")
//    public Page<Book> getFilteredByAuthor(@PathVariable String authorName, @NotNull final Pageable pageable) {
//        return bookService.getAllFilteredByAuthor(pageable, authorName);
//    }

    @GetMapping(path = "/{id}")
    public Page<Book> getBook(@PathVariable int id, @NotNull final Pageable pageable) {
        Page<Book> result = bookRepository.findBookById(id, pageable);
        if (!result.isEmpty())
            return result;
        else
            throw new EntityNotFoundException(String.format("Book with id %d doesn't exist!", id));
    }

    @GetMapping(path = "/title/{title}")
    public Page<Book> getBookByTitle(@PathVariable String title, @NotNull final Pageable pageable) {
        return bookRepository.findBookByTitle(title, pageable);
    }

    @GetMapping(path = "/genre/{genre}")
    public Page<Book> getBooksByGenre(@PathVariable Genre genre, @NotNull final Pageable pageable) {
        return bookRepository.findBooksByGenresContaining(genre, pageable);
    }

    @GetMapping(path = "/author/{name}")
    public Page<Book> getBooksByAuthor(@PathVariable String name, @NotNull final Pageable pageable) {
        return bookService.getBooksByAuthorName(name, pageable);
    }

    // http://localhost:8080/api/books/type/AUDIO
    @GetMapping(path = "/type/{type}")
    public Page<Book> getByBookType(@PathVariable BookType type, @NotNull final Pageable pageable) {
        return bookRepository.findBooksByBookType(type, pageable);
    }

    @GetMapping(path = "/by-owned")
    public Page<Book> getByOwned(@RequestParam boolean owned, @NotNull final Pageable pageable) {
        return bookRepository.findBooksByOwned(owned, pageable);
    }

    // http://localhost:8080/api/books/by-finished?finished=false
    @GetMapping(path = "/by-finished")
    public Page<Book> getByFinished(@RequestParam boolean finished, @NotNull final Pageable pageable) {
        return bookRepository.findBooksByFinished(finished, pageable);
    }

    @GetMapping(path = "/loved")
    public Page<Book> getLoved(@NotNull final Pageable pageable) {
        return bookRepository.findBooksByLovedTrue(pageable);
    }

    /**
     *  ============================NON-IDEMPOTENT=====================================
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addNewBook(@RequestBody Book newBook) {
        checkNew(newBook);
        Book created = bookRepository.save(newBook);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable int id) {
        bookRepository.deleteBookById(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@RequestBody Book book) {
        bookRepository.save(book);
    }

    // TODO: get ? post ? put ? patch ?
    @PutMapping(path = "/finish/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleBookFinished(@PathVariable int id) {
       bookService.toggleBookFinished(id);
    }

    // TODO: get ? post ? put ? patch ?
    @PutMapping(path = "/owned/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleBookOwned(@PathVariable int id) {
        bookService.toggleBookOwned(id);
    }

}
