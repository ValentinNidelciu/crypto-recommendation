package ro.assignment.cryptorec.exposition.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetails {
    private String message;
    private String developerMessage;
}
