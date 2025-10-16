package bard.service;

import bard.dao.CurrencyRepository;
import bard.dao.ExchangeRateRepository;
import bard.exception.exchangeRate.InvalidExchangeRateException;
import bard.model.Currency;
import bard.model.ExchangeRate;
import bard.model.ExchangeRateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExchangeRateService {
    @Autowired
    public ExchangeRateRepository exchangeRateRepository;
    @Autowired
    public CurrencyRepository currencyRepository;

    public List<ExchangeRate> getExchangeRates() {
        return exchangeRateRepository.getExchangeRates();
    }

    public ExchangeRate getExchangeRateByCode(String currencyPair) {
        if (currencyPair == null || currencyPair.length() != 6) {
            throw new IllegalArgumentException("Currency pair must be exactly 6 characters");
        }

        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        return exchangeRateRepository.getExchangeRateByCodes(baseCode, targetCode);
    }

    public ExchangeRate createExchangeRate(ExchangeRateRequest request) { //нужно ли валидирывать данные, которые приходят как-то подругому?

        Currency baseCurrency = currencyRepository.getCurrencyByCode(request.getBaseCurrencyCode());
        Currency targetCurrency = currencyRepository.getCurrencyByCode(request.getTargetCurrencyCode());

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, request.getRate());
        return exchangeRateRepository.postExchangeRate(exchangeRate);
    }

    public ExchangeRate updateExchangeRate(String baseCode, String targetCode, BigDecimal newRate) {
        if (newRate == null || newRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidExchangeRateException(String.valueOf(newRate));
        }

        return exchangeRateRepository.updateExchangeRate(baseCode, targetCode, newRate);
    }

    public boolean isExistsExchangeRate(String baseCurrencyCode, String targetCurrencyCode) {
        return exchangeRateRepository.isExistsExchangeRate(baseCurrencyCode, targetCurrencyCode);
    }

}
