package bard.exception.currency;

import bard.exception.ExchangeException;
import org.springframework.http.HttpStatus;

public class CurrencyNotFoundException extends ExchangeException {
    public CurrencyNotFoundException(String currencyName) {
        super("Currency not found: " + currencyName, "CURRENCY_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public CurrencyNotFoundException(Long currencyId) {
        super("Currency not found with ID: " + currencyId, "CURRENCY_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}