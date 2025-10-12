package bard.exception;


import org.springframework.http.HttpStatus;

public class InvalidRequestException extends ExchangeException {
    public InvalidRequestException(String message) {
        super("Invalid request: " + message, "INVALID_REQUEST", HttpStatus.BAD_REQUEST);
    }
}