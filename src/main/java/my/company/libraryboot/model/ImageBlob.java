package my.company.libraryboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "covers")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ImageBlob extends BaseEntity {

    @JsonIgnoreProperties("coverImage")
    @OneToOne(mappedBy = "coverImage")
    private Book book;

    @Lob
    @Column(name = "image", columnDefinition = "bytea") // "columnDefinition" defines the column type
    private byte[] image;

    @Override
    public String toString() {
        return String.format("Book cover[%d]", this.id);
    }
}
