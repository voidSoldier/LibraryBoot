package my.company.libraryboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import my.company.libraryboot.model.enums.Gender;

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
//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")
public class Author extends BaseEntity {

    @Column(name = "name", nullable = false)
    @NotEmpty
    @Size(max = 255)
    private String name;

    @Column(name = "country_of_origin")
    private String country;

//    @JsonIgnore
//    @JsonBackReference
    @JsonIgnoreProperties("authors") // to avoid Jackson JSON infinite recursion
    @ManyToMany(mappedBy = "authors", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Book> books;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Override
    public String toString() {
        return String.format("Author[%s]", this.name);
    }
}
