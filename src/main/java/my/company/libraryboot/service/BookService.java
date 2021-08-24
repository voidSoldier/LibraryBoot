package my.company.libraryboot.service;

import my.company.libraryboot.exception.AppException.EntityNotFoundException;
import my.company.libraryboot.model.Book;
import my.company.libraryboot.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        return bookRepository.getAll(pageable);
    }

    public Page<Book> getBooksByAuthorName(String authorName, Pageable pageable) {
        if (authorName.split(" ").length > 1)
            return bookRepository.getBooksByAuthorFullName(authorName, pageable);
        else
            return bookRepository.getBooksByAuthorName(authorName, pageable);
    }

    public void toggleBookFinished(int id) {
        Book book = getBookById(id);
        book.setFinished(!book.isFinished());
        bookRepository.save(book);
    }

    public void toggleBookOwned(int id) {
        Book book = getBookById(id);
        book.setOwned(!book.isOwned());
        bookRepository.save(book);
    }

    public Page<Book> getBookById(int id, Pageable pageable) throws EntityNotFoundException {
        Page<Book> result = bookRepository.findBookById(id, pageable);

        if (!result.isEmpty())
            return result;
        else throw new EntityNotFoundException(String.format("Book with id %d doesn't exist!", id));
    }

    public Book getBookById(int id) {
        return getBookById(id, Pageable.unpaged()).getContent().get(0);
    }

}
