package my.company.libraryboot.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

public class BookCoverImageUploadingException extends AppException {

    public BookCoverImageUploadingException(HttpStatus status, String message, ErrorAttributeOptions options) {
        super(status, message, options);
    }

    public BookCoverImageUploadingException(String msg) {
        this(HttpStatus.UNPROCESSABLE_ENTITY, msg, ErrorAttributeOptions.of(MESSAGE));
    }
}
