package ro.assignment.cryptorec.application.exception;

public class ComputeNormalizedRangeException extends RuntimeException {
    public ComputeNormalizedRangeException() {
        super("There was a problem computing the highest normalized range. Please try again later.");
    }
}
