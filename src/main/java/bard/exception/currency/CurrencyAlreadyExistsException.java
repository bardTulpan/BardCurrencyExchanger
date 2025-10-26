package bard.exception.currency;

import bard.exception.ExchangeException;
import org.springframework.http.HttpStatus;

public class CurrencyAlreadyExistsException extends ExchangeException {
    public CurrencyAlreadyExistsException(String currencyCode) {
        super("Currency already exists: " + currencyCode, "CURRENCY_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}