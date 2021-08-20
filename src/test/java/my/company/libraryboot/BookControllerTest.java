package my.company.libraryboot;

import my.company.libraryboot.model.Author;
import my.company.libraryboot.model.Book;
import my.company.libraryboot.model.ImageBlob;
import my.company.libraryboot.repository.BookRepository;
import my.company.libraryboot.repository.ImageBlobRepository;
import my.company.libraryboot.service.BookService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.function.Predicate;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookControllerTest extends AbstractControllerTest {

    @Autowired
    private BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    ImageBlobRepository imageRepository;

    private static final String URL = "/api/books";
    private static final String TEST_AUTHOR_NAME = "aber";

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getById() throws Exception {
        MvcResult mvcResult = perform(MockMvcRequestBuilders.get(URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Book found = objectMapper.readValue(extractJsonObj(mvcResult.getResponse().getContentAsString()), Book.class);

        if (found != null)
            Assertions.assertEquals(1, found.getId());
    }

    @Test
    @Transactional
    void uploadBookCover() throws Exception {
        CsrfToken csrfToken = getCsrfToken();

        MvcResult mvcResult = perform(MockMvcRequestBuilders.get(URL + "/1")).andReturn();
        Book book = objectMapper.readValue(extractJsonObj(mvcResult.getResponse().getContentAsString()), Book.class);

        BufferedImage bufferedImage = ImageIO.read(new FileImageInputStream(new File("src\\main\\resources\\bookcover.jpg")));
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", output);
        byte[] cover = output.toByteArray();

        ImageBlob bookCover = new ImageBlob(book, cover);
        imageRepository.save(bookCover);

        book.setCoverImage(bookCover);

        perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(book))
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()));

        Book bookWithCover = bookService.getBookById(1);

        Assertions.assertNotNull(bookWithCover.getCoverImage());
        Assertions.assertEquals(bookWithCover.getCoverImage(), bookCover);
    }

    @Test
    void getFinishedBooks() throws Exception {
        MvcResult mvcResult = perform(MockMvcRequestBuilders.get(URL + "/finished?finished=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Object> found = objectMapper.readerFor(Book.class)
                .readValues(extractJsonObjArray(mvcResult.getResponse().getContentAsString()))
                .readAll();

        if (!found.isEmpty())
            Assertions.assertTrue(((Book)found.get(0)).isFinished());
    }

    @Test
    void getBooksByAuthor() throws Exception {
        MvcResult mvcResult = perform(MockMvcRequestBuilders.get(URL + "/author/" + TEST_AUTHOR_NAME))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        List<Object> found = objectMapper.readerFor(Book.class)
                .readValues(extractJsonObjArray(mvcResult.getResponse().getContentAsString()))
                .readAll();

        Predicate<Author> testAuthorName = a ->
                (a.getFirstName() + a.getLastName()).toLowerCase().contains(TEST_AUTHOR_NAME);

        if (!found.isEmpty())
            Assertions.assertTrue(((Book) found.get(0))
                    .getAuthors().stream()
                    .anyMatch(testAuthorName)
            );
    }
}
