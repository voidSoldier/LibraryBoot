package my.company.libraryboot.service;

import my.company.libraryboot.error.EntityNotFoundException;
import my.company.libraryboot.model.Author;
import my.company.libraryboot.model.Book;
import my.company.libraryboot.repository.BookRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    private static final String SORT_ASC = "asc";

    public Page<Book> getAllSorted(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        Sort.Direction dir = SORT_ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(dir, sortBy));
        return bookRepository.findAll(pageable);
    }

    public Page<Book> getAllFilteredByAuthor(Pageable pageable, String authorName) {
        List<Book> filtered = bookRepository.findAll().stream().filter(b -> {
            Set<Author> authors = b.getAuthors();
            for (Author a : authors)
                if ((a.getFirstName() + " " + a.getLastName()).toLowerCase().contains(authorName.toLowerCase()))
                    return true;
            return false;
        }).collect(Collectors.toList());

        Page<Book> result = new PageImpl<>(filtered, pageable, filtered.size());
        return result;
    }

    public Page<Book> getBooksByAuthorName(String authorName, Pageable pageable) {
        if (authorName.split(" ").length > 1)
            return bookRepository.getBooksByAuthorFullName(authorName, pageable);
        else
            return bookRepository.getBooksByAuthorName(authorName, pageable);
    }

    public void toggleBookFinished(int id) {
        Book book = getBookByIdUnpaged(id);
        book.setFinished(!book.isFinished());
        bookRepository.save(book);
    }

    public void toggleBookOwned(int id) {
        Book book = getBookByIdUnpaged(id);
        book.setOwned(!book.isOwned());
        bookRepository.save(book);
    }

    public Page<Book> getBookById(int id, Pageable pageable) throws EntityNotFoundException {
        Page<Book> result = bookRepository.findBookById(id, pageable);

        if (!result.isEmpty())
            return result;
        else throw new EntityNotFoundException(String.format("Book with id %d doesn't exist!", id));
    }

    private Book getBookByIdUnpaged(int id) {
        return getBookById(id, Pageable.unpaged()).getContent().get(0);
    }
}
