package bard.exception.exchangeRate;

import bard.exception.ExchangeException;
import org.springframework.http.HttpStatus;

public class ExchangeRateNotFoundException extends ExchangeException {
    public ExchangeRateNotFoundException(Long exchangeRateId) {
        super("Exchange rate not found with ID: " + exchangeRateId,
                "EXCHANGE_RATE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public ExchangeRateNotFoundException(String baseCurrency, String targetCurrency) {
        super("Exchange rate not found: " + baseCurrency + " to " + targetCurrency,
                "EXCHANGE_RATE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    public ExchangeRateNotFoundException(String currencyPair) {
        super("Exchange rate not found for currency pair: " + currencyPair,
                "EXCHANGE_RATE_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}