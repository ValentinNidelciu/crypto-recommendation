package ro.assignment.cryptorec.application.exception;

public class DataNotAvailableException extends RuntimeException {
    public DataNotAvailableException() {
        super("We currently don't have any data for your coin. Please try again with another valid symbol.");
    }
}
