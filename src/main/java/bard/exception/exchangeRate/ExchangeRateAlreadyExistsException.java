package bard.exception.exchangeRate;

import bard.exception.ExchangeException;
import org.springframework.http.HttpStatus;

public class ExchangeRateAlreadyExistsException extends ExchangeException {
    public ExchangeRateAlreadyExistsException(String baseCurrency, String targetCurrency) {
        super("Exchange rate already exists: " + baseCurrency + " to " + targetCurrency,
                "EXCHANGE_RATE_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}