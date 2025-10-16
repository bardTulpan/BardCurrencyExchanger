package bard.dao;

import bard.model.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRateImpl {
    List<ExchangeRate> getExchangeRates();

    ExchangeRate getExchangeRateByCodes(String a, String b);

    ExchangeRate postExchangeRate(ExchangeRate exchangeRate);

    ExchangeRate updateExchangeRate(String baseCode, String targetCode, BigDecimal newRate);

    boolean isExistsExchangeRate(String baseCurrencyCode, String targetCurrencyCode);
}

