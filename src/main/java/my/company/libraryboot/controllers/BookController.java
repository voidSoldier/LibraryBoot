package my.company.libraryboot.controllers;

import lombok.extern.slf4j.Slf4j;
import my.company.libraryboot.exception.AppException;
import my.company.libraryboot.exception.AppException.BookCoverImageUploadingException;
import my.company.libraryboot.exception.AppException.EntityNotFoundException;
import my.company.libraryboot.model.Book;
import my.company.libraryboot.model.ImageBlob;
import my.company.libraryboot.model.enums.BookType;
import my.company.libraryboot.model.enums.Genre;
import my.company.libraryboot.repository.BookRepository;
import my.company.libraryboot.repository.ImageBlobRepository;
import my.company.libraryboot.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;

import static my.company.libraryboot.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = BookController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class BookController {

    public static final String REST_URL = "/api/books";
    BookRepository bookRepository;
    BookService bookService;
    ImageBlobRepository imageRepository;

    public BookController(BookRepository bookRepository, BookService bookService, ImageBlobRepository imageRepository) {
        this.bookRepository = bookRepository;
        this.bookService = bookService;
        this.imageRepository = imageRepository;
    }

    @GetMapping()
    public Page<Book> getAll(@NotNull final Pageable pageable) {
        log.info("getting all books");
        return bookRepository.getAll(pageable);
    }

    // http://localhost:8080/api/books/sorted?pageNo=0&pageSize=10&sortBy=finished
    @GetMapping(path = "/sorted")
    public Page<Book> getAllSortedByParam(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "desc") String direction)
    {
        log.info("getting books sorted by {}", sortBy);
        return bookService.getAllSorted(pageNo, pageSize, sortBy, direction);
    }

    @GetMapping(path = "/{id}")
    public Page<Book> getBook(@PathVariable int id, @NotNull final Pageable pageable) {
       log.info("getting book {}", id);
       return bookService.getBookById(id, pageable);
    }

    @GetMapping(path = "/title/{title}")
    public Page<Book> getBookByTitle(@PathVariable String title, @NotNull final Pageable pageable) {
        log.info("getting books with title {}", title);
        return bookRepository.findBookByTitle(title, pageable);
    }

    @GetMapping(path = "/genre/{genre}")
    public Page<Book> getBooksByGenre(@PathVariable Genre genre, @NotNull final Pageable pageable) {
        log.info("getting books by genre {}", genre);
        return bookRepository.findBooksByGenresContaining(genre, pageable);
    }

    @GetMapping(path = "/author/{name}")
    public Page<Book> getBooksByAuthor(@PathVariable String name, @NotNull final Pageable pageable) {
        log.info("getting books by author {}", name);
        return bookService.getBooksByAuthorName(name, pageable);
    }

    // http://localhost:8080/api/books/type/AUDIO
    @GetMapping(path = "/type/{type}")
    public Page<Book> getByBookType(@PathVariable BookType type, @NotNull final Pageable pageable) {
        log.info("getting books by type {}", type);
        return bookRepository.findBooksByBookType(type, pageable);
    }

    @GetMapping(path = "/owned")
    public Page<Book> getByOwned(@RequestParam boolean owned, @NotNull final Pageable pageable) {
        log.info("getting owned books");
        return bookRepository.findBooksByOwned(owned, pageable);
    }

    // http://localhost:8080/api/books/finished?finished=false
    @GetMapping(path = "/finished")
    public Page<Book> getByFinished(@RequestParam boolean finished, @NotNull final Pageable pageable) {
        log.info("getting finished books");
        return bookRepository.findBooksByFinished(finished, pageable);
    }

    @GetMapping(path = "/loved")
    public Page<Book> getLoved(@NotNull final Pageable pageable) {
        log.info("getting loved books");
        return bookRepository.findBooksByLovedTrue(pageable);
    }

    @PostMapping(path = "/upload-image/{bookId}")
    public void uploadCoverImage(@RequestParam("file") MultipartFile cover, @PathVariable int bookId) throws BookCoverImageUploadingException {
        log.info("uploading cover image to book {}", bookId);
        Book book = bookRepository.findBookById(bookId, Pageable.unpaged()).getContent().get(0);

        if (book != null) {
            try {
                ImageBlob bookCover = new ImageBlob(book, cover.getBytes());
                imageRepository.save(bookCover);
                book.setCoverImage(bookCover);
                bookRepository.save(book);
            } catch (IOException e) {
                throw new BookCoverImageUploadingException(
                        String.format("Error uploading cover image for Book with id %d", bookId));
            }
        } else throw new AppException.EntityNotFoundException(String.format("Book with id %d doesn't exist!", bookId));
    }

    @GetMapping(path = "/get-book-cover/{bookId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getBookCoverImage(@PathVariable int bookId) {
        log.info("getting cover image of book {}", bookId);
        ImageBlob cover = imageRepository.findImageBlobByBook_Id(bookId);

        if (cover != null)
            return cover.getImage();
        else throw new EntityNotFoundException(String.format("Book with id %d doesn't exist OR it has no cover image!", bookId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> addNewBook(@RequestBody Book newBook) {
        checkNew(newBook);
        Book created = bookRepository.save(newBook);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        log.info("book added {}", created);
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable int id) {
        log.info("deleting book {}", id);
        bookRepository.deleteBookById(id);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateBook(@RequestBody Book book) {
        log.info("updating book {}", book);
        bookRepository.save(book);
    }

    @PatchMapping(path = "/finish/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleBookFinished(@PathVariable int id) {
       log.info("toggling book finished {}", id);
       bookService.toggleBookFinished(id);
    }

    @PatchMapping(path = "/owned/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleBookOwned(@PathVariable int id) {
        log.info("toggling book owned {}", id);
        bookService.toggleBookOwned(id);
    }
}
