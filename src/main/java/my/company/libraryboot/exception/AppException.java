package my.company.libraryboot.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

@Getter
@Slf4j
public class AppException extends ResponseStatusException {
    private final ErrorAttributeOptions options;

    public AppException(HttpStatus status, String message, ErrorAttributeOptions options) {
        super(status, message);
        this.options = options;
    }


    public static class BookCoverImageUploadingException extends AppException {
        public BookCoverImageUploadingException(String msg) {
            super(HttpStatus.UNPROCESSABLE_ENTITY, msg, ErrorAttributeOptions.of(MESSAGE));
            log.error("BookCoverImageUploadingException : {}", msg);
        }
    }


    public static class EntityNotFoundException extends AppException {
        public EntityNotFoundException(String msg) {
            super(HttpStatus.NOT_FOUND, msg, ErrorAttributeOptions.of(MESSAGE));
            log.error("EntityNotFoundException : {}", msg);
        }
    }


    public static class IllegalRequestDataException extends AppException {
        public IllegalRequestDataException(String msg) {
            super(HttpStatus.UNPROCESSABLE_ENTITY, msg, ErrorAttributeOptions.of(MESSAGE));
            log.error("IllegalRequestDataException : {}", msg);
        }
    }


    public static class RoleNotFoundException extends AppException {
        public RoleNotFoundException(String msg) {
            super(HttpStatus.UNPROCESSABLE_ENTITY, msg, ErrorAttributeOptions.of(MESSAGE));
            log.error("RoleNotFoundException : {}", msg);
        }
    }

}
