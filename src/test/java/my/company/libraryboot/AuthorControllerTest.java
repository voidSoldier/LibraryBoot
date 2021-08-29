package my.company.libraryboot;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import my.company.libraryboot.model.Author;
import my.company.libraryboot.model.enums.Gender;
import my.company.libraryboot.repository.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthorControllerTest extends AbstractControllerTest {

    @Autowired
    AuthorRepository authorRepository;

    private static final String URL = "/api/authors";

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
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        Author found = objectMapper.readValue(extractJsonObj(mvcResult.getResponse().getContentAsString()), Author.class);

        if (found != null)
            Assertions.assertEquals(1, found.getId());
    }

    @Test
    void addNewAuthor() throws Exception {
        CsrfToken csrfToken = getCsrfToken();
        Author newAuthor = new Author("firstName", "lastName", Gender.F, "Italy", new HashSet<>());

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
        String authorAsRequestBody = writer.writeValueAsString(newAuthor);

        MvcResult mvcResult = perform(MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(authorAsRequestBody)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(status().isCreated())
                .andReturn();

        Author addedAuthor = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Author.class);
        newAuthor.setId(addedAuthor.getId());
        Assertions.assertEquals(newAuthor, addedAuthor);

    }

    @Test
    void deleteAuthor() throws Exception {
        CsrfToken csrfToken = getCsrfToken();

        perform(MockMvcRequestBuilders.delete(URL + "/1")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.get(URL + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAuthor() throws Exception {
        CsrfToken csrfToken = getCsrfToken();
        String updatedFirstName = "updatedFirstName";

        Author author = authorRepository.findAuthorById(1, Pageable.unpaged()).getContent().get(0);
        author.setFirstName(updatedFirstName);

        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
        String authorAsRequestBody = writer.writeValueAsString(author);

        perform(MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(authorAsRequestBody)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(status().isNoContent());

        Author updatedAuthor = authorRepository.findAuthorById(1, Pageable.unpaged()).getContent().get(0);

        Assertions.assertEquals(updatedFirstName, updatedAuthor.getFirstName());
    }
}
