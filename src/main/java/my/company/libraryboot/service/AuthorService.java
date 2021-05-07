package my.company.libraryboot.service;

import my.company.libraryboot.model.Author;
import my.company.libraryboot.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Page<Author> getAllSorted(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return authorRepository.findAll(pageable);
    }

    public Page<Author> getBooksByAuthorName(String authorName, Pageable pageable) {
        if (authorName.split(" ").length > 1)
            return authorRepository.getBooksByAuthorFullName(authorName, pageable);
        else
            return authorRepository.getBooksByAuthorName(authorName, pageable);
    }
}
