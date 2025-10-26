package bard.service;

import bard.dao.CurrencyRepository;
import bard.dao.ExchangeRateRepository;
import bard.exception.InvalidRequestException;
import bard.exception.MissingException;
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
    public final ExchangeRateRepository exchangeRateRepository;
    public final CurrencyRepository currencyRepository;

    @Autowired
    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository, CurrencyRepository currencyRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.currencyRepository = currencyRepository;
    }

    public void isValidExchangeRateRequest(ExchangeRateRequest exchangeRateRequest) {
        if (exchangeRateRequest.getBaseCurrencyCode() == null || exchangeRateRequest.getBaseCurrencyCode().trim().isEmpty()) {
            throw new InvalidRequestException("Missing required field: baseCurrencyCode");
        }
        if (exchangeRateRequest.getTargetCurrencyCode() == null || exchangeRateRequest.getTargetCurrencyCode().trim().isEmpty()) {
            throw new InvalidRequestException("Missing required field: targetCurrencyCode");
        }
        if (exchangeRateRequest.getRate() == null) {
            throw new InvalidRequestException("Missing required field: rate");
        }
    }


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

    public boolean isValidCurrencyPair(String currencyPair) {
        if (currencyPair == null || currencyPair.trim().isEmpty()) {
            throw new MissingException("Currency pair can not be empty");
        }
        return true;
    }

    public ExchangeRate createExchangeRate(ExchangeRateRequest request) {

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
        return exchangeRateRepository.isExchangeRateExists(baseCurrencyCode, targetCurrencyCode);
    }

}
