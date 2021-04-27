package my.company.libraryboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "authors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Author extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotEmpty
    @Size(max = 255)
    private String name;

    @Column(name = "country_of_origin")
    private String country;

    @JsonIgnore
    @ManyToMany(mappedBy = "authors", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Book> books;

    @Override
    public String toString() {
        return String.format("Author[%s]", this.name);
    }
}
