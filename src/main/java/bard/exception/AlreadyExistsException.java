package bard.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends ExchangeException {
    public AlreadyExistsException(String message) {
        super("Already exists: " + message, "ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
