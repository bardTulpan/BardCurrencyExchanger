package bard.exception;

import org.springframework.http.HttpStatus;

// Конкретные исключения
public class ExchangeNotFoundException extends ExchangeException {
    public ExchangeNotFoundException(String currencyName) {
        super("Currency not found: " + currencyName, "CURRENCY_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
