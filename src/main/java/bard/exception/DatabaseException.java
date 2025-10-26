package bard.exception;

import org.springframework.http.HttpStatus;

public class DatabaseException extends ExchangeException {
    public DatabaseException(String message) {
        super("Database error: " + message, "DATABASE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
