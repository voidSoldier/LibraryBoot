package my.company.libraryboot.repository;

import my.company.libraryboot.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    int deleteUserById(int id);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Page<User> findUserById(int id, Pageable page);
}
