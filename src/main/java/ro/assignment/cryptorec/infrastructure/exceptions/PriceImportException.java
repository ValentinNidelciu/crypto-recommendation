package ro.assignment.cryptorec.infrastructure.exceptions;

public class PriceImportException extends RuntimeException {
    public PriceImportException(final String message) {
        super(message);
    }
}
