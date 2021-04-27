package my.company.libraryboot.model;

import lombok.*;
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

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Genre> genres = new HashSet<>();

    //    @CollectionTable(name = "authors", joinColumns = @JoinColumn(name = "book_id"))
//    @Column(name = "authors")
//    @ElementCollection(fetch = FetchType.EAGER)

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
