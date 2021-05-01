package my.company.libraryboot.controllers;

import my.company.libraryboot.model.Book;
import my.company.libraryboot.repository.BookRepository;
import my.company.libraryboot.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping(value = BookTemplateController.REST_URL)
public class BookTemplateController {

    public static final String REST_URL = "/api/thymeleaf/books";
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookService bookService;

    @GetMapping()
    public String getAllWithThymeleaf(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping(path = "/sort-by-title")
    public String getAllSortByTitleWithThymeleaf(Model model) {
        List<Book> sorted = bookRepository.findAll();
        sorted.sort(Comparator.comparing(Book::getTitle));
        model.addAttribute("books", sorted);
        return "books";
    }

    @GetMapping(path = "/filter-by-author")
    public String getFilteredByAuthorWithThymeleaf(@RequestParam String authorName, Model model) {
        List<Book> filtered = bookService.getAllFilteredByAuthor(Pageable.unpaged(), authorName).getContent();
        model.addAttribute("books", filtered);
        return "books";
    }
}
