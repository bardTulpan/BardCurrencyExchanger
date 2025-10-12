package bard.exception.exchangeRate;

import bard.exception.ExchangeException;
import org.springframework.http.HttpStatus;

public class InvalidExchangeRateException extends ExchangeException {
    public InvalidExchangeRateException(String message) {
        super("Invalid exchange rate: " + message,
                "INVALID_EXCHANGE_RATE", HttpStatus.BAD_REQUEST);
    }

    public InvalidExchangeRateException(Double rate) {
        super("Exchange rate must be positive: " + rate,
                "INVALID_EXCHANGE_RATE", HttpStatus.BAD_REQUEST);
    }
}