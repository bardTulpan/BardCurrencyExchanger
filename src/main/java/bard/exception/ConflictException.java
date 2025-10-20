package bard.exception;

import bard.exception.ExchangeException;
import org.springframework.http.HttpStatus;

public class ConflictException extends ExchangeException {
    public ConflictException(String message) {
        super("ConflictException" + message, "ConflictException", HttpStatus.BAD_REQUEST);
    }
}