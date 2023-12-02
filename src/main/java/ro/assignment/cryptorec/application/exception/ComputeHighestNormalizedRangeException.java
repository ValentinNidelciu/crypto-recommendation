package ro.assignment.cryptorec.application.exception;

public class ComputeHighestNormalizedRangeException extends RuntimeException {
    public ComputeHighestNormalizedRangeException() {
        super("There was a problem computing the highest normalized range. Please try again later.");
    }
}
