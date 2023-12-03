package ro.assignment.cryptorec.exposition.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ro.assignment.cryptorec.application.exception.ComputeNormalizedRangeException;
import ro.assignment.cryptorec.application.exception.DataNotAvailableException;
import ro.assignment.cryptorec.infrastructure.exceptions.PriceImportException;

@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = PriceImportException.class)
    public ResponseEntity<ErrorDetails> handle(final PriceImportException exception) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        "There was a problem with importing the latest prices. Please try again later.",
                        exception.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(value = DataNotAvailableException.class)
    public ResponseEntity<ErrorDetails> handle(final DataNotAvailableException exception) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        "We currently don't have any data for your coin. Please try again with another valid symbol. Or try reimporting the data",
                        exception.getMessage()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(value = ComputeNormalizedRangeException.class)
    public ResponseEntity<ErrorDetails> handle(final ComputeNormalizedRangeException exception) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        "There was a problem computing the highest normalized range. Please try again later.",
                        exception.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorDetails> handle(final RuntimeException exception) {
        return new ResponseEntity<>(
                new ErrorDetails(
                        "A problem has occurred. Please try again later.",
                        exception.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
