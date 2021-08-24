package my.company.libraryboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
//https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing-spring-boot-applications-testing-with-mock-environment
@WithUserDetails(userDetailsServiceBeanName = "userDetailsService", value = "admin@gmail.com")
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    private static final String START = "[";
    private static final String END = "]";
    private static final String OBJ_TEMPLATE = "{%s}";
    protected static final String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";

    protected ResultActions perform(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        return mockMvc.perform(requestBuilder);
    }

   protected static String extractJsonObj(String response) {
        int start = response.indexOf(START) + 2;
        int end = response.lastIndexOf(END) + 1;

        return String.format(OBJ_TEMPLATE, response.substring(start, end));
   }

   protected static String extractJsonObjArray(String array) {
        return START + extractJsonObj(array) + END;
   }

   protected static CsrfToken getCsrfToken() {
       HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
       return httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
   }

}
