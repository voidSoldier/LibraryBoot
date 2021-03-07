package my.company.libraryboot.repository;

import my.company.libraryboot.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AuthorRepository extends JpaRepository<Author, Integer> {

}
