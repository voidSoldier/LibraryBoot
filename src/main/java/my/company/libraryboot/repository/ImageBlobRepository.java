package my.company.libraryboot.repository;

import my.company.libraryboot.model.ImageBlob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
public interface ImageBlobRepository extends JpaRepository<ImageBlob, Integer> {

    ImageBlob findImageBlobByBook_Id(int id);
}
