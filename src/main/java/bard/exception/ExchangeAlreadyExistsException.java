package bard.exception;

import org.springframework.http.HttpStatus;

public class ExchangeAlreadyExistsException extends ExchangeException {
    public ExchangeAlreadyExistsException(String currencyCode) {
        super("Currency already exists: " + currencyCode, "CURRENCY_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
