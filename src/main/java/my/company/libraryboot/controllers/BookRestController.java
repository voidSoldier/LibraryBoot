package my.company.libraryboot.controllers;

import my.company.libraryboot.error.EntityNotFoundException;
import my.company.libraryboot.model.Book;
import my.company.libraryboot.model.Genre;
import my.company.libraryboot.repository.BookRepository;
import my.company.libraryboot.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Comparator;
import java.util.List;

import static my.company.libraryboot.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = BookRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class BookRestController {

    public static final String REST_URL = "/api/books";
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookService bookService;

    @GetMapping(path = "/thymleaf")
    public String getAllWithThymleaf(Model model) {
      List<Book> books = bookRepository.findAll();
      model.addAttribute("books", books);
      return "books";
    }

//    @GetMapping()
//    public ResponseEntity<Page<Book>> getAll(@NotNull final Pageable pageable) {
//        Page<Book> books = bookRepository.findAll(pageable);
//        return ResponseEntity.ok(books);
//    }

    @GetMapping()
    public ResponseEntity<Page<Book>> getAll(@NotNull final Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping(path = "/sort-by-title/thymleaf")
    public String getAllSortByTitleWithThymleaf(Model model) {
        List<Book> sorted = bookRepository.findAll();
        sorted.sort(Comparator.comparing(Book::getTitle));
        model.addAttribute("books", sorted);
        return "books";
    }

    // http://localhost:8080/api/books/sorted?pageNo=0&pageSize=10&sortBy=finished
    // TODO: add ascending or descending order
    @GetMapping(path = "/sorted")
    public ResponseEntity<Page<Book>> getAllSorted(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "title") String sortBy)
    {
        Page<Book> books = bookService.getAllSorted(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(books);
    }

    @GetMapping(path = "/filter-by-author/{authorName}")
    public ResponseEntity<Page<Book>> getFilteredByAuthor(@PathVariable String authorName, @NotNull final Pageable pageable) {
        Page<Book> result = bookService.getAllFilteredByAuthor(pageable, authorName);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/filter-by-author/thymleaf")
    public String getFilteredByAuthorWithThymleaf(@RequestParam String authorName, Model model) {
        List<Book> filtered = bookService.getAllFilteredByAuthor(Pageable.unpaged(), authorName).getContent();
        model.addAttribute("books", filtered);
        return "books";
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Page<Book>> getBook(@PathVariable int id, @NotNull final Pageable pageable) {
        Page<Book> result = bookRepository.findBookById(id, pageable);
        if (!result.isEmpty())
            return ResponseEntity.ok(result);
        else
            throw new EntityNotFoundException(String.format("Book with id %d doesn't exist!", id));
    }

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

    @PutMapping(path = "/{id}/finish", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finishBook(@RequestBody Book book) {
        book.setFinished(true);
        bookRepository.save(book);
    }

    // TODO: works without ResponseEntity<Book> WHY??????
    @GetMapping(path = "/title/{title}")
    public Page<Book> getBookByTitle(@PathVariable String title, @NotNull final Pageable pageable) {
        return bookRepository.findBookByTitle(title, pageable);
    }

    @GetMapping(path = "/genre/{genre}")
    public Page<Book> getBooksByGenre(@PathVariable Genre genre, @NotNull final Pageable pageable) {
        return bookRepository.findBooksByGenresContaining(genre, pageable);
    }

    // TODO: doesn't work via sql request. Only with request results filtering
    @GetMapping(path = "/author/{name}")
    Page<Book> getBooksByAuthor(@PathVariable String name, @NotNull final Pageable pageable) {
        return bookRepository.getBooksByAuthorName(name, pageable);
    }
}
