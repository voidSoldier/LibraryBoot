package my.company.libraryboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import my.company.libraryboot.model.enums.BookType;
import my.company.libraryboot.model.enums.Genre;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Book extends BaseEntity {

    @Column(name = "title", nullable = false)
    @NotEmpty
    private String title = Strings.EMPTY;

    @Column(name = "finished")
    private boolean finished = false;

    @Column(name = "owned")
    private boolean owned;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_type")
    private BookType bookType;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Genre> genres = new HashSet<>();

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @Override
    public String toString() {
        return String.format("Book[%s]", this.title);
    }
}
