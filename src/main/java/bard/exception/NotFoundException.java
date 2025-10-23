package bard.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ExchangeException {
    public NotFoundException(String currencyName) {
        super("Not found: " + currencyName, "NOT_FOUND", HttpStatus.NOT_FOUND);
    }

}