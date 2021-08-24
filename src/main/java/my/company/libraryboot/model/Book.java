package my.company.libraryboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import my.company.libraryboot.model.enums.BookType;
import my.company.libraryboot.model.enums.Genre;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @Column(name = "loved")
    private boolean loved;

    @Column(name = "note")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "book_type")
    private BookType bookType;

    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "genres", joinColumns = @JoinColumn(name = "book_id"),
            uniqueConstraints ={@UniqueConstraint(columnNames = {"book_id", "genre"}, name = "book_genres_unique")})
    @Column(name = "genre")
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Genre> genres;

    @JsonIgnoreProperties("books") // to avoid Jackson JSON infinite recursion
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @JsonIgnoreProperties({"book", "image"}) // doesn't affect performance: fields are still loaded from DB, just not sent by controller
    @OneToOne(cascade = CascadeType.PERSIST) // FetchType.EAGER set by default; FetchType.LAZY is unlikely possible: https://stackoverflow.com/a/1445694
    @JoinTable(name = "book_covers",
            joinColumns = @JoinColumn(name="book_id"),
            inverseJoinColumns = @JoinColumn(name="image_id"))
    private ImageBlob coverImage;




    @Override
    public String toString() {
        return String.format("Book[%s]", this.title);
    }
}
