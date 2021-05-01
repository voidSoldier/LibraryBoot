package my.company.libraryboot.service;

import my.company.libraryboot.model.Author;
import my.company.libraryboot.model.Book;
import my.company.libraryboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    public Page<Book> getAllSorted(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
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
}
