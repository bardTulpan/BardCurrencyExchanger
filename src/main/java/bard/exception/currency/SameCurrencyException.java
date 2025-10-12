package bard.exception.currency;

import bard.exception.ExchangeException;
import org.springframework.http.HttpStatus;

public class SameCurrencyException extends ExchangeException {
    public SameCurrencyException(String currencyCode) {
        super("Base and target currencies cannot be the same: " + currencyCode,
                "SAME_CURRENCY", HttpStatus.BAD_REQUEST);
    }
}