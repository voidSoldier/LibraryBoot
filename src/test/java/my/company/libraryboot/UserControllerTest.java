package my.company.libraryboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import my.company.libraryboot.model.User;
import my.company.libraryboot.model.enums.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerTest extends AbstractControllerTest {

    private static final String URL = "/api/users";

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getById() throws Exception {
       User found = getUserById("/1");

       if (found != null)
            Assertions.assertEquals(1, found.getId());
    }

    @Test
    void addNewRole() throws Exception {
        CsrfToken csrfToken = getCsrfToken();
        User user = getUserById("/1");
        String userAsRequestBody = getUserWithPasswordAsString(user);

        perform(MockMvcRequestBuilders.put(URL + "/add-role/" + Role.TEST)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(userAsRequestBody)
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken()))
                .andExpect(status().isOk());

        User userWithNewRole = getUserById("/1");
        Assertions.assertTrue(userWithNewRole.getRoles().contains(Role.TEST));
    }

    private User getUserById(String id) throws Exception {
        MvcResult mvcResult = perform(MockMvcRequestBuilders.get(URL + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        return objectMapper.readValue(extractJsonObj(mvcResult.getResponse().getContentAsString()), User.class);
    }

    private String getUserWithPasswordAsString(User user) throws JsonProcessingException {
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = objectMapper.writer().withDefaultPrettyPrinter();
        Map<String, Object> userMap = objectMapper.convertValue(user, new TypeReference<Map<String, Object>>() {});
        userMap.put("password", "new password");

        return writer.writeValueAsString(userMap);
    }
}
