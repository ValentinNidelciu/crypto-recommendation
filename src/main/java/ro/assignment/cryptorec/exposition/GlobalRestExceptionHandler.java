package ro.assignment.cryptorec.exposition;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.assignment.cryptorec.infrastructure.exceptions.PriceImportException;

@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = PriceImportException.class)
    public ResponseEntity<String> handle(final PriceImportException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<String> handle(final RuntimeException exception) {
        return new ResponseEntity<>("A problem has occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
